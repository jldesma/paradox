package com.paradox.services

import com.google.gson.Gson
import com.google.protobuf.ByteString
import com.paradox.service.*
import io.provenance.client.grpc.GasEstimationMethod
import io.provenance.client.grpc.PbClient
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api/blockchain")
class ParadoxController {
    val CONTRACT_ADDRESS = "tp1jpuk5d0auylc7c7dkmds0auzadt7d39tw85fkudzs94y53mavvxs0jg5sr"
    val client = PbClient("pio-testnet-1", URI("grpcs://grpc.test.provenance.io:443"), GasEstimationMethod.MSG_FEE_CALCULATION)
    val gson = Gson()

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/GetByToken/{token_id}")
    fun GetByToken(@PathVariable token_id: String): CurrentAskForTokenResponse {
        val result = bcQuery(ByteString.copyFromUtf8("""{"current_ask_for_token": { "token_id" : "${token_id}" }}"""), client, CONTRACT_ADDRESS);

        return gson.fromJson(result, CurrentAskForTokenResponse::class.java);
    }

    @GetMapping("/Test/{mnemonic}")
    fun Test(@PathVariable mnemonic: String): String {
        val adminSigner = WalletSigner(NetworkType.TESTNET, mnemonic)
        val addr = adminSigner.address()

        return addr
    }

    @GetMapping("/GetByOwner/{address}")
    fun GetByOwner(@PathVariable address:String): Array<MyMintMsg> {
        val result =  bcQuery(ByteString.copyFromUtf8("""{"tokens_of": { "owner" : "${address}" }}"""), client, CONTRACT_ADDRESS);

        return gson.fromJson(result, Array<MyMintMsg>::class.java);
    }

    @PostMapping
    fun Insert(@RequestBody obj: InvoiceViewModel): String {
        logger.warn("LLEGO ESTOOO!!")

        val adminSigner = WalletSigner(NetworkType.TESTNET, obj.mnemonic)
        obj.invoice.account_address = adminSigner.address()

        val data = MintMsg (
                MyMintMsg(
                        token_id = UUID.randomUUID().toString(),
                        owner = adminSigner.address(),
                        token_uri = "",
                        extension = obj.invoice
                ),
                Coin("token",obj.invoice.invoice.toString())
        )

        val json:String = gson.toJson(data)
        val result =  bcExecute(ByteString.copyFromUtf8("""{"mint":{ "mint": ${json} }}"""),obj.mnemonic, client, CONTRACT_ADDRESS);

        logger.warn("TERMINOO ESTOOO!!")

        return result
    }

    @PutMapping
    fun Update(@RequestBody obj: InvoiceViewModel): String {
        val json:String = gson.toJson(obj.invoice)
        val result =  bcExecute(ByteString.copyFromUtf8("""{"update_data":{ "obj": ${json} }}"""), obj.mnemonic, client, CONTRACT_ADDRESS);

        return result
    }
}