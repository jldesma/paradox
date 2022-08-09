package io.provenance.spec

import io.provenance.scope.util.toUuid

object PayableSpecification : AssetSpecification {

    override val scopeSpecConfig = ScopeSpecConfig(
        id = "11e7d541-a120-48a7-98c5-e32b8aa0707f".toUuid(),
        name = "Payable NFT",
        description = "Payable NFT Specification provided by the Provenance Blockchain Foundation",
        websiteUrl = websiteUrl
    )

    override val contractSpecConfigs = listOf(
        ContractSpecConfig(
            id = "a965a903-af84-4322-a645-d11ceb55c17f".toUuid(),
            contractClassname = "io.provenance.spec.OnboardPayableContractSpec",
            name = "Onboard Payable NFT",
            description = "Mint Payable NFT Contract Specification provided by the Provenance Blockchain Foundation",
            websiteUrl = websiteUrl
        )
    )

    override val recordSpecConfigs = listOf(
        RecordSpecConfig(
            id = "2bd4e986-0964-46bb-a8b3-8d4989a287e2".toUuid(),
            name = "payable",
            typeClassname = "io.provenance.model.v1.Asset",
            contractSpecId = contractSpecConfigs.first().id
        )
    )

}
