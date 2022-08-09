package io.provenance.spec

import io.provenance.spec.util.SpecBuilder

val AssetSpecifications: List<AssetSpecification> = listOf(
    PropertySpecification,
)


interface AssetSpecification {

    val scopeSpecConfig: ScopeSpecConfig
    val contractSpecConfigs: List<ContractSpecConfig>
    val recordSpecConfigs: List<RecordSpecConfig>

    fun specificationMsgs(ownerAddress: String) = SpecBuilder.buildMetadataSpecificationTransaction(
        ownerAddress = ownerAddress,
        scopeSpec = scopeSpecConfig,
        recordSpecList = recordSpecConfigs,
        contractSpecList = contractSpecConfigs
    )
}