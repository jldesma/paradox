package io.provenance.spec

import io.provenance.scope.util.toUuid

object PersonalLoanSpecification : AssetSpecification {

    override val scopeSpecConfig = ScopeSpecConfig(
        id = "61ca59d2-7a11-4ca7-8035-eef698656dc8".toUuid(),
        name = "Personal Loan NFT",
        description = "Personal Loan NFT Specification provided by the Provenance Blockchain Foundation",
        websiteUrl = websiteUrl
    )

    override val contractSpecConfigs = listOf(
        ContractSpecConfig(
            id = "746b698c-f63d-489e-9b42-8c61a7316c62".toUuid(),
            contractClassname = "io.provenance.spec.OnboardPersonal LoanContractSpec",
            name = "Onboard Personal Loan NFT",
            description = "Mint Personal Loan NFT Contract Specification provided by the Provenance Blockchain Foundation",
            websiteUrl = websiteUrl
        )
    )

    override val recordSpecConfigs = listOf(
        RecordSpecConfig(
            id = "93c4af8a-fa32-4bd9-8207-005fd52bcbda".toUuid(),
            name = "personalLoan",
            typeClassname = "io.provenance.model.v1.Asset",
            contractSpecId = contractSpecConfigs.first().id
        )
    )
}
