# GenesisApp

Mobile Application, which allows the user to create, deploy, utilize and obtain tokens.
It uses the Genesis Library. If you are interested in the library, check out: www.github.com/FuturICT2/Genesis. In case of questions,  please contact: bmark@ethz.ch.

# Installation
* clone the repository to your pc
* install adroid studio on our pc
* open the project with android studio
* Connect a phone through usb to your pc or start an emulator
* Run the project, it will be deployed to your phone or emulator

# Functions of GenesisApp

The app currently consists of four views. These will be explained in the following. 

## Token crator

The creator view allows the user to create a new token with different properties, underlyings and operations. Currently only basic properties can be defined:
* name: Name of the Token
* symbol: Short string representation of the token name. I.e. BTC is the symbol of bitcoin
* Maximum supply: Token cap. I.e. bitcoin has a token cap of 21 million. 
* Decimals: How many decimal position should the token have. I.e. Euro has two (100 cents)
* Genesis supply: Number of tokens which were pre-mined

In Challenge 1: You can use this view to directly store your created tokens to the blockchain of your chosing. 

## Wallet

In future all different types of tokens will be displayed and the balance of the user will be shown. Currently, it only lists the tokens existing in the local SQLite datase

## Token Obtainer
In future, the user will be able to select a token in this view. This token will then dipslay an action (task), which the user has to perform. If the action is accepted as true, the user will be rewarded with tokens.

In Challenge 2, you can use this view to test your implementation and to integrate it with other parts of the tool.

## Projects
In future, the user will be able to trade his or her
tokens in this view. Moreover, projects can be listed, on which the user can spend his or her tokens (crowdsourcing etc.)

# First Run
* after starting the app, you will see your empty wallet
* swipe left to the Creator
* For the moment, you can specify in this view the (basic) properties of the token you want to create. Make sure to save each input.
* After saving the Token ("save Token"-button), it will be shown in the wallet overview. Just swipe back (right) to the wallet view.

![hallo](https://github.com/FuturICT2/Genesis/blob/master/screenCreator.png)

# Software Architecture
