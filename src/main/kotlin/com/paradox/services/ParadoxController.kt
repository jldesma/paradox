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

    @GetMapping("/Test/{token_id}")
    fun Test(@PathVariable token_id: String): String {
        return "Tomas"
    }

    @GetMapping("/GetByOwner/{address}")
    fun GetByOwner(@PathVariable address:String): Array<MyMintMsg> {
        val result =  bcQuery(ByteString.copyFromUtf8("""{"tokens_of": { "owner" : "${address}" }}"""), client, CONTRACT_ADDRESS);

        return gson.fromJson(result, Array<MyMintMsg>::class.java);
    }

    @CrossOrigin
    @PostMapping
    fun Insert(@RequestBody invoice: InvoiceNTF): String {
        logger.warn("LLEGO ESTOOO!!")
        val data = MintMsg (
                MyMintMsg(
                        token_id = UUID.randomUUID().toString(),
                        owner = invoice.account_address,
                        token_uri = "",
                        extension = invoice
                ),
                Coin("token",invoice.invoice.toString())
        )

        val json:String = gson.toJson(data)
        val result =  bcExecute(ByteString.copyFromUtf8("""{"mint":{ "mint": ${json} }}"""), "century draft give hazard assault swing attract civil rescue enable model annual session alcohol income utility alley urge play stove silver practice stumble jewel", client, CONTRACT_ADDRESS);

        logger.warn("TERMINOO ESTOOO!!")

        return result
    }

    @PutMapping
    fun Update(@RequestBody invoice: InvoiceNTF): String {
        val json:String = gson.toJson(invoice)
        val result =  bcExecute(ByteString.copyFromUtf8("""{"update_data":{ "obj": ${json} }}"""), "century draft give hazard assault swing attract civil rescue enable model annual session alcohol income utility alley urge play stove silver practice stumble jewel", client, CONTRACT_ADDRESS);

        return result
    }
}