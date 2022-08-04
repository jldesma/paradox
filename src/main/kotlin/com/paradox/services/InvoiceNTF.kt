package com.paradox.service

import io.provenance.hdwallet.bip39.MnemonicWords

data class Data(val data: Array<InvoiceNTF>) { }

data class InvoiceViewModel(val invoice: InvoiceNTF,val mnemonic: String){}
data class InvoiceNTF(
        var account_address: String,
        var invoice: Int,
        var issue_date: String,
        var terms: String,
        var due_date: String,
        var booking: Int,
        var item: String,
        var amount: String,
        var zip: String,
        var writer: String,
        var project_title: String,
        var bank_name: String,
        var bank_address: String,
        var acct_no: String,
        var routing_no: String,
        var swift_code: String,
        var note: String,
        var phone: String) { }


data class MintMsg(val base:MyMintMsg,
                    val ask_amount:Coin) {}

data class Coin(val denom:String,val amount:String) {}

data class MyMintMsg(val token_id: String,
                     val owner: String,
                     val token_uri: String,
                     var extension:InvoiceNTF) {}


data class CurrentAskForTokenResponse(val ask:Ask) {}
data class Ask(val amount:Coin) {}

data class BidForTokenBidderResponse(val bid:Bid) {}
data class Bid(val amount:Coin, val bidder:String) {}