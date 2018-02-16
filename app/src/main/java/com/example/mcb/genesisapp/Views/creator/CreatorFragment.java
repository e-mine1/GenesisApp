package com.example.mcb.genesisapp.Views.creator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mcb.genesisapp.R;
import com.example.mcb.genesisapp.Repository.emineback.EMineBackend;
import com.example.mcb.genesisapp.State.StateCallback;

import io.reactivex.functions.Consumer;

/**
 * Fragment holding the logic for creating a new Token
 */
public class CreatorFragment extends Fragment implements StateCallback.StateListener {

    Handler handler = new Handler(Looper.getMainLooper());
    private View createButton;
    private EditText tokenNameText;
    private EditText tokenSymbolText;
    private EditText tokenMaxSupplyText;
    private EditText numberOfDecimalsText;
    private EditText genesisSupplyText;
    private Spinner tokenTypeSpinner;
    private EMineBackend backend = new EMineBackend();

    public CreatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatorFragment newInstance() {
        CreatorFragment fragment = new CreatorFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_obtainer, container, false);

        return view;

    }

    private boolean checkIfNotValidAndShowToast(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("is invalid: " + fieldName);
            Toast.makeText(this.getContext(), "" + fieldName + " can not be empty.",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        System.out.println("is valid: " + fieldName);
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.createButton = getView().findViewById(R.id.createContractButton);
        this.tokenNameText = getView().findViewById(R.id.tokenNameText);
        this.tokenSymbolText = getView().findViewById(R.id.tokenSymbolText);
        this.tokenMaxSupplyText = getView().findViewById(R.id.maxSupplyText);
        this.numberOfDecimalsText = getView().findViewById(R.id.decimalsText);
        this.genesisSupplyText = getView().findViewById(R.id.genesisSupplyText);
        this.tokenTypeSpinner = getView().findViewById(R.id.contractType_spinner2);

        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tokenName = tokenNameText.getText().toString();
                if (checkIfNotValidAndShowToast(tokenName, "Token Name")) return;

                String symbol = tokenSymbolText.getText().toString();
                if (checkIfNotValidAndShowToast(symbol, "Symbol")) return;

                String totalSupplyStr = tokenMaxSupplyText.getText().toString();
                int totalSupply = -1;
                try {
                    totalSupply = Integer.getInteger(totalSupplyStr);
                } catch (Exception e) {
                }

                String initSupplyStr = genesisSupplyText.getText().toString();
                int initSupply = 0;
                try {
                    initSupply = Integer.getInteger(genesisSupplyText.getText().toString());
                } catch (Exception e) {
                }

                String decimalsStr = numberOfDecimalsText.getText().toString();
                int decimals = 0;
                try {
                    decimals = Integer.getInteger(numberOfDecimalsText.getText().toString());
                } catch (Exception e) {
                }

                final String tokenType = tokenTypeSpinner.getSelectedItem().toString();
                if (checkIfNotValidAndShowToast(tokenType, "Token Type")) return;

                backend.createTokenObservable(new EMineBackend.
                        CreateTokenRequest(tokenName, symbol, totalSupply, initSupply, decimals, tokenType))
                        .subscribe(new Consumer<EMineBackend.CreateTokenResponse>() {
                            @Override
                            public void accept(EMineBackend.CreateTokenResponse createTokenResponse)
                                    throws Exception {
                                System.out.println(createTokenResponse.getKey());

                                final String requestKey = createTokenResponse.getKey();

                                final ProgressDialog dialog = ProgressDialog.show(getContext(), "Compiling ...",
                                        String.format("Your Token %s is being compiled and " +
                                                "deployed", tokenName, tokenType), false);

                                Toast.makeText(getContext(), "Token issued. "
                                        + createTokenResponse.getKey(), Toast.LENGTH_LONG).show();

                                new Thread() {
                                    public void run() {
                                        try {
                                            final Wrapper<Boolean> tryagain = new Wrapper<Boolean>();
                                            final Wrapper<EMineBackend.CreateTokenResponse> response = new Wrapper<>();
                                            tryagain.type = true;
                                            do {
                                                Thread.sleep(1000);
                                                backend.fetchRequestStatus(requestKey)
                                                        .subscribe(new Consumer<EMineBackend.CreateTokenResponse>() {
                                                            @Override
                                                            public void accept(EMineBackend.CreateTokenResponse createTokenResponse) throws Exception {
                                                                if (createTokenResponse.getStatus().equals("success")) {
                                                                    tryagain.type = false;
                                                                    response.type = createTokenResponse;
                                                                    dialog.dismiss();
                                                                }
                                                            }
                                                        });
                                            } while (tryagain.type);
                                            System.out.println("success");

                                            handler.post(new Runnable() {
                                                public void run() {
                                                    MaterialDialog b = new MaterialDialog.Builder(getContext())
                                                            .title("Success :D")
                                                            .content("Your contract was compiled and deployed to the network." +
                                                                    " \n\nContract Address:\n\n"
                                                                    + response.type.getTokenAddr())
                                                            .positiveText("Ok")
                                                            .negativeText("More Information")
//                                                            .negativeText("Browse ABI
                                                            .build();
                                                    b.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                        @Override
                                                        public void onCancel(DialogInterface dialogInterface) {
                                                            backend.getRequestInformationUrl(requestKey);
                                                            String url = backend.getRequestInformationUrl(requestKey);
                                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                            startActivity(browserIntent);
                                                        }
                                                    });
                                                    b.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialogInterface) {

                                                        }
                                                    });
                                                    b.show();
                                                }
                                            });

                                        } catch (Exception e) {
                                        }
                                    }
                                }.start();
                            }
                        });
            }
        });
    }

    public void createContract(View view) {

    }

    static class Wrapper<T> {
        public T type;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void dataBaseModified(String type_of_object, String subTypeObject) {

    }
}
