package io.provenance.spec.util

import com.google.protobuf.Message
import io.provenance.metadata.v1.DefinitionType
import io.provenance.metadata.v1.InputSpecification
import io.provenance.metadata.v1.MsgWriteContractSpecificationRequest
import io.provenance.metadata.v1.MsgWriteRecordSpecificationRequest
import io.provenance.metadata.v1.MsgWriteScopeSpecificationRequest
import io.provenance.metadata.v1.PartyType
import io.provenance.scope.util.MetadataAddress
import io.provenance.scope.util.toByteString
import io.provenance.spec.ContractSpecConfig
import io.provenance.spec.RecordSpecConfig
import io.provenance.spec.ScopeSpecConfig

object SpecBuilder {

    // Builds the Provenance metadata transaction for writing contract/scope/record specifications to the chain
    fun buildMetadataSpecificationTransaction(
        ownerAddress: String,
        scopeSpec: ScopeSpecConfig,
        recordSpecList: List<RecordSpecConfig>,
        contractSpecList: List<ContractSpecConfig>,
    ): List<Message> {

        val contractSpecsMsgs = buildContractSpecs(contractSpecList, ownerAddress)
        val scopeSpecMsg = buildScopeSpec(ownerAddress, scopeSpec, contractSpecsMsgs)
        val recordSpecMsgs = buildRecordSpecs(ownerAddress, recordSpecList)

        return (contractSpecsMsgs + scopeSpecMsg + recordSpecMsgs)

    }


    private fun buildScopeSpec(
        owner: String,
        scopeSpec: ScopeSpecConfig,
        contractSpecsMsgs: List<MsgWriteContractSpecificationRequest>
    ): MsgWriteScopeSpecificationRequest =

        MsgWriteScopeSpecificationRequest.newBuilder().apply {
            addSigners(owner)
            specUuid = scopeSpec.id.toString()
            specificationBuilder.apply {
                descriptionBuilder.apply {
                    name = scopeSpec.name
                    description = scopeSpec.description
                    websiteUrl = scopeSpec.websiteUrl
                }
            }
                .addAllContractSpecIds(
                    contractSpecsMsgs.map { it.specification.specificationId }
                )
                .addAllOwnerAddresses(listOf(owner))
                .addAllPartiesInvolved(
                    listOf(
                        PartyType.PARTY_TYPE_OWNER
                    )
                )
        }.build()


    private fun buildRecordSpecs(
        owner: String,
        recordSpecs: List<RecordSpecConfig>
    ): List<MsgWriteRecordSpecificationRequest> = recordSpecs.map { recordSpec ->
        MsgWriteRecordSpecificationRequest.newBuilder().apply {
            addSigners(owner)
            contractSpecUuid = recordSpec.contractSpecId.toString()
            specificationBuilder
                .setName(recordSpec.name)
                .setTypeName(recordSpec.typeClassname)
                .setSpecificationId(
                    MetadataAddress.forRecordSpecification(recordSpec.contractSpecId, recordSpec.name).bytes.toByteString()
                )
                .setResultType(DefinitionType.DEFINITION_TYPE_RECORD)
                .addAllResponsibleParties(
                    listOf(
                        PartyType.PARTY_TYPE_OWNER
                    )
                )
                .addInputs(
                    InputSpecification.newBuilder().apply {
                        name = recordSpec.name
                        typeName = recordSpec.typeClassname
                        hash = recordSpec.name
                    }
                )
        }.build()
    }

    fun buildContractSpecs(
        contractSpecList: List<ContractSpecConfig>,
        owner: String
    ): List<MsgWriteContractSpecificationRequest> = contractSpecList.map { contractSpec ->

        MsgWriteContractSpecificationRequest.newBuilder().apply {
            addSigners(owner)
            specificationBuilder
                .setSpecificationId(MetadataAddress.forContractSpecification(contractSpec.id).bytes.toByteString())
                .setClassName(contractSpec.contractClassname)
                .setHash(contractSpec.contractClassname)
                .addAllOwnerAddresses(listOf(owner))
                .addAllPartiesInvolved(
                    listOf(
                        PartyType.PARTY_TYPE_OWNER
                    )
                )
        }.build()
    }
}
