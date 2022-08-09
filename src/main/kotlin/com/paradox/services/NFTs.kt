package com.paradox.services

import com.google.protobuf.ByteString
import com.google.protobuf.Message
import com.paradox.service.WalletSigner
import com.paradox.service.toAny
import cosmos.crypto.secp256k1.Keys
import io.provenance.client.grpc.GasEstimationMethod
import io.provenance.client.grpc.PbClient
import io.provenance.client.grpc.Signer
import io.provenance.client.wallet.NetworkType
import io.provenance.hdwallet.bip39.MnemonicWords
import io.provenance.hdwallet.wallet.Account
import io.provenance.hdwallet.wallet.Wallet
import io.provenance.metadata.v1.*
import io.provenance.scope.util.MetadataAddress
import io.provenance.scope.util.ProtoJsonUtil.toJson
import io.provenance.scope.util.toByteString
import java.net.URI
import java.util.*

import cosmos.tx.v1beta1.ServiceOuterClass
import cosmos.tx.v1beta1.TxOuterClass
import cosmwasm.wasm.v1.Tx
import io.provenance.client.grpc.BaseReqSigner
import io.provenance.client.protobuf.extensions.getBaseAccount
import io.provenance.client.protobuf.extensions.toAny
import io.provenance.client.protobuf.extensions.toTxBody
import io.provenance.metadata.v1.*
import io.provenance.spec.AssetSpecification
import io.provenance.spec.AssetSpecifications
import io.provenance.spec.PropertySpecification


    fun bcExecuteNFT(mnemonic: String, pbClient: PbClient, ownerAddress: String): String {
        val signer =  WalletSigner(NetworkType.TESTNET, mnemonic)

        val txn = AssetSpecifications
                .flatMap { makeExampleNFT(ownerAddress, it) }
                .map { it.toAny() }
                .toTxBody()

        pbClient.estimateAndBroadcastTx(
                txBody = txn,
                signers = listOf(BaseReqSigner(signer)),
                mode = ServiceOuterClass.BroadcastMode.BROADCAST_MODE_BLOCK,
                gasAdjustment = 1.2
        ).also {
            println("Response:")
            println(it.toJson())
        }

        return "";
    }

    fun makeExampleNFT(ownerAddress: String, assetSpec: AssetSpecification): List<Message> {

        val scopeId = UUID.randomUUID()
        val sessionId: UUID = UUID.randomUUID()
        val contractSpec = assetSpec.contractSpecConfigs.first()
        val recordSpec = assetSpec.recordSpecConfigs.first()

        return listOf(

                // write-scope
                MsgWriteScopeRequest.newBuilder().apply {
                    addSigners(ownerAddress)
                    scopeUuid = scopeId.toString()
                    specUuid = assetSpec.scopeSpecConfig.id.toString()
                    scopeBuilder.setScopeId(MetadataAddress.forScope(scopeId).bytes.toByteString())
                                .setValueOwnerAddress(ownerAddress)
                                .addAllOwners(
                                    listOf(
                                            Party.newBuilder().apply {
                                                address = ownerAddress
                                                role = PartyType.PARTY_TYPE_OWNER
                                            }.build()
                                    )
                            )
                }.build(),

                // write-session
                MsgWriteSessionRequest.newBuilder().apply {
                    addSigners(ownerAddress)
                    specUuid = contractSpec.id.toString()
                    sessionIdComponentsBuilder
                            .setScopeUuid(scopeId.toString())
                            .setSessionUuid(sessionId.toString())
                    sessionBuilder
                            .setSessionId(MetadataAddress.forSession(scopeId, sessionId).bytes.toByteString())
                            .addParties(Party.newBuilder().apply {
                                address = ownerAddress
                                role = PartyType.PARTY_TYPE_OWNER
                            })
                            .auditBuilder
                            .setCreatedBy(ownerAddress)
                            .setUpdatedBy(ownerAddress)
                }.build(),

                // write-record
                MsgWriteRecordRequest.newBuilder().apply {
                    addSigners(ownerAddress)
                    contractSpecUuid = contractSpec.id.toString()
                    recordBuilder
                            .setSessionId(MetadataAddress.forSession(scopeId, sessionId).bytes.toByteString())
                            .setSpecificationId(
                                    MetadataAddress.forRecordSpecification(
                                            contractSpec.id,
                                            recordSpec.name
                                    ).bytes.toByteString()
                            )
                            .setName(recordSpec.name)
                            .addInputs(
                                    RecordInput.newBuilder().apply {
                                        name = recordSpec.name
                                        typeName = recordSpec.typeClassname
                                        hash = "Fake data hash"
                                        status = RecordInputStatus.RECORD_INPUT_STATUS_PROPOSED
                                    }.build()
                            )
                            .addOutputs(
                                    RecordOutput.newBuilder().apply {
                                        hash = "Fake data hash"
                                        status = ResultStatus.RESULT_STATUS_PASS
                                    }.build()
                            )
                            .processBuilder
                            .setName(contractSpec.contractClassname)
                            .setMethod(contractSpec.contractClassname)
                            .setHash("Not even sure what this hash field is for")
                }.build(),

                )
    }


