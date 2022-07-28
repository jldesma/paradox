package com.paradox.service

import com.google.protobuf.ByteString
import cosmos.tx.v1beta1.ServiceOuterClass
import cosmos.tx.v1beta1.TxOuterClass
import cosmwasm.wasm.v1.Tx
import io.provenance.client.grpc.BaseReqSigner
import io.provenance.client.grpc.PbClient
import io.provenance.client.protobuf.extensions.getBaseAccount

fun bcExecute(data: ByteString, mnemonic: String, client: PbClient, CONTRACT_ADDRESS: String): String {
    val adminMnemonic = mnemonic;

    val adminSigner = WalletSigner(NetworkType.TESTNET, adminMnemonic)
    val adminAccount = client.authClient.getBaseAccount(adminSigner.address())
    var adminOffset = 0

    val executeMatchMsg = Tx.MsgExecuteContract.newBuilder()
            .setContract(CONTRACT_ADDRESS)
            .setMsg(data)
            .setSender(adminSigner.address())
            .build().toAny()

    client.estimateAndBroadcastTx(
            TxOuterClass.TxBody.newBuilder().addMessages(executeMatchMsg).build(),
            listOf(BaseReqSigner(adminSigner, adminOffset, adminAccount)),
            ServiceOuterClass.BroadcastMode.BROADCAST_MODE_BLOCK).also {
        if (it.txResponse.code != 0) {
            throw Exception("Error matching scope ask/bid, code: ${it.txResponse.code}, message: ${it.txResponse.rawLog}")
        }

        adminOffset++
        return it.txResponse.txhash
    }
}