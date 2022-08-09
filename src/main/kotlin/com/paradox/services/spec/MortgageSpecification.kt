package io.provenance.spec

import io.provenance.scope.util.toUuid

object MortgageSpecification : AssetSpecification {

    override val scopeSpecConfig = ScopeSpecConfig(
        id = "86fef324-a9d0-4d00-ae2f-e03a85c8224c".toUuid(),
        name = "Mortgage NFT",
        description = "Mortgage NFT Specification provided by the Provenance Blockchain Foundation",
        websiteUrl = websiteUrl
    )

    override val contractSpecConfigs = listOf(
        ContractSpecConfig(
            id = "f4ac5c9c-a220-4403-85d3-9f0eb62b8bc1".toUuid(),
            contractClassname = "io.provenance.spec.OnboardMortgageContractSpec",
            name = "Onboard Mortgage NFT",
            description = "Mint Mortgage NFT Contract Specification provided by the Provenance Blockchain Foundation",
            websiteUrl = websiteUrl
        )
    )

    override val recordSpecConfigs = listOf(
        RecordSpecConfig(
            id = "25bb5aa7-8bbe-43d6-a48c-900c0b772ed1".toUuid(),
            name = "mortgage",
            typeClassname = "io.provenance.model.v1.Asset",
            contractSpecId = contractSpecConfigs.first().id
        )
    )
}
