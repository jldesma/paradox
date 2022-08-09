package io.provenance.spec

import io.provenance.scope.util.toUuid

object CollectableSpecification : AssetSpecification {

    override val scopeSpecConfig = ScopeSpecConfig(
        id = "6efad216-5a9a-4c7d-b3e9-7e4550ca8cf2".toUuid(),
        name = "Collectable NFT",
        description = "Collectable NFT Specification provided by the Provenance Blockchain Foundation",
        websiteUrl = websiteUrl
    )

    override val contractSpecConfigs = listOf(
        ContractSpecConfig(
            id = "1bef03c6-3f86-481b-927b-5dbec22a7ab2".toUuid(),
            contractClassname = "io.provenance.spec.OnboardCollectableContractSpec",
            name = "Onboard Collectable NFT",
            description = "Mint Collectable NFT Contract Specification provided by the Provenance Blockchain Foundation",
            websiteUrl = websiteUrl
        )
    )

    override val recordSpecConfigs = listOf(
        RecordSpecConfig(
            id = "73fdbb5d-4234-40e6-abef-dd6c044b4237".toUuid(),
            name = "collectable",
            typeClassname = "io.provenance.model.v1.Asset",
            contractSpecId = contractSpecConfigs.first().id
        )
    )

}
