package io.provenance.spec

import io.provenance.scope.util.toUuid

object


PropertySpecification : AssetSpecification {

    override val scopeSpecConfig = ScopeSpecConfig(
        id = "79c47c7b-9206-470c-b9bc-f6d80e1ffdda".toUuid(),
        name = "Property NFT",
        description = "Property NFT Specification provided by the Provenance Blockchain Foundation",
        websiteUrl = websiteUrl
    )

    override val contractSpecConfigs = listOf(
        ContractSpecConfig(
            id = "ffe52f9c-482f-439c-8788-ad20cdf14338".toUuid(),
            contractClassname = "io.provenance.spec.OnboardPropertyContractSpec",
            name = "Onboard Property NFT",
            description = "Mint Property NFT Contract Specification provided by the Provenance Blockchain Foundation",
            websiteUrl = websiteUrl
        )
    )

    override val recordSpecConfigs = listOf(
        RecordSpecConfig(
            id = "8b834744-01a4-4638-9261-cc4b2e19dd9b".toUuid(),
            name = "property",
            typeClassname = "io.provenance.model.v1.Asset",
            contractSpecId = contractSpecConfigs.first().id
        )
    )
}
