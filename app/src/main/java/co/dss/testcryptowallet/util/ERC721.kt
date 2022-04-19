package co.dss.testcryptowallet.util

import org.web3j.protocol.Web3j
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.EventEncoder
import io.reactivex.Flowable
import io.reactivex.functions.Function
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import java.math.BigInteger
import java.util.*

/**
 *
 * Auto generated code.
 *
 * **Do not modify!**
 *
 * Please use the [web3j command line tools](https://docs.web3j.io/command_line.html),
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * [codegen module](https://github.com/web3j/web3j/tree/master/codegen) to update.
 *
 *
 * Generated with web3j version 4.1.1.
 */
class ERC721 : Contract {
    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        credentials: Credentials?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?
    ) : super(
        BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit
    ) {
    }

    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        credentials: Credentials?,
        contractGasProvider: ContractGasProvider?
    ) : super(
        BINARY, contractAddress, web3j, credentials, contractGasProvider
    ) {
    }

    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        transactionManager: TransactionManager?,
        gasPrice: BigInteger?,
        gasLimit: BigInteger?
    ) : super(
        BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit
    ) {
    }

    protected constructor(
        contractAddress: String?,
        web3j: Web3j?,
        transactionManager: TransactionManager?,
        contractGasProvider: ContractGasProvider?
    ) : super(
        BINARY, contractAddress, web3j, transactionManager, contractGasProvider
    ) {
    }

    //return the address of the owner of the token
    fun ownerOf(_tokenId: BigInteger?): RemoteCall<String> {
        val function = Function(
            FUNC_OWNEROF,
            Arrays.asList<Type<*>>(Uint256(_tokenId)),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    //return number of NFTs owner by an address.
    fun balanceOf(_owner: String?): RemoteCall<BigInteger> {
        val function = Function(
            FUNC_BALANCEOF,
            Arrays.asList<Type<*>>(Address(_owner)),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Uint256?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    //This function grants or approves another entity the permission to transfer tokens on the owner’s behalf.
    fun approve(
        _approved: String?,
        _tokenId: BigInteger?,
        weiValue: BigInteger?
    ): RemoteCall<TransactionReceipt> {
        val function = Function(
            FUNC_APPROVE,
            Arrays.asList<Type<*>>(
                Address(_approved),
                Uint256(_tokenId)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function, weiValue)
    }

    fun getApproved(_tokenId: BigInteger?): RemoteCall<String> {
        val function = Function(
            FUNC_GETAPPROVED,
            Arrays.asList<Type<*>>(Uint256(_tokenId)),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun setApprovalForAll(_operator: String?, _approved: Boolean?): RemoteCall<TransactionReceipt> {
        val function = Function(
            FUNC_SETAPPROVALFORALL,
            Arrays.asList<Type<*>>(
                Address(_operator),
                Bool(_approved)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    //it allows the owner to transfer the token to another user
    fun transferFrom(
        _from: String?,
        _to: String?,
        _tokenId: BigInteger?,
        weiValue: BigInteger?
    ): RemoteCall<TransactionReceipt> {
        val function = Function(
            FUNC_TRANSFERFROM,
            listOf<Type<*>>(
                Address(_from),
                Address(_to),
                Uint256(_tokenId)
            ),
            emptyList()
        )
        return executeRemoteCallTransaction(function, weiValue)
    }

    fun safeTransferFrom(
        _from: String?,
        _to: String?,
        _tokenId: BigInteger?,
        weiValue: BigInteger?
    ): RemoteCall<TransactionReceipt> {
        val function = Function(
            FUNC_SAFETRANSFERFROM,
            listOf<Type<*>>(
                Address(_from),
                Address(_to),
                Uint256(_tokenId)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function, weiValue)
    }

    fun safeTransferFrom(
        _from: String?,
        _to: String?,
        _tokenId: BigInteger?,
        data: ByteArray?,
        weiValue: BigInteger?
    ): RemoteCall<TransactionReceipt> {
        val function = Function(
            FUNC_SAFETRANSFERFROM,
            Arrays.asList<Type<*>>(
                Address(_from),
                Address(_to),
                Uint256(_tokenId),
                DynamicBytes(data)
            ), emptyList()
        )
        return executeRemoteCallTransaction(function, weiValue)
    }

    fun isApprovedForAll(_owner: String?, _operator: String?): RemoteCall<Boolean> {
        val function = Function(
            FUNC_ISAPPROVEDFORALL,
            Arrays.asList<Type<*>>(
                Address(_owner),
                Address(_operator)
            ),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Bool?>() {})
        )
        return executeRemoteCallSingleValueReturn(function, Boolean::class.java)
    }

    fun getTransferEvents(transactionReceipt: TransactionReceipt?): List<TransferEventResponse> {
        val valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt)
        val responses = ArrayList<TransferEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = TransferEventResponse()
            typedResponse.log = eventValues.log
            typedResponse._from = eventValues.indexedValues[0].value as String
            typedResponse._to = eventValues.indexedValues[1].value as String
            typedResponse._tokenId = eventValues.indexedValues[2].value as BigInteger
            responses.add(typedResponse)
        }
        return responses
    }

    fun transferEventFlowable(filter: EthFilter?): Flowable<TransferEventResponse> {
        return web3j.ethLogFlowable(filter).map(object : Function<Log?, TransferEventResponse> {
            override fun apply(log: Log): TransferEventResponse {
                val eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log)
                val typedResponse = TransferEventResponse()
                typedResponse.log = log
                typedResponse._from = eventValues.indexedValues[0].value as String
                typedResponse._to = eventValues.indexedValues[1].value as String
                typedResponse._tokenId = eventValues.indexedValues[2].value as BigInteger
                return typedResponse
            }
        })
    }

    fun transferEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<TransferEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT))
        return transferEventFlowable(filter)
    }

    fun getApprovalEvents(transactionReceipt: TransactionReceipt?): List<ApprovalEventResponse> {
        val valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt)
        val responses = ArrayList<ApprovalEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = ApprovalEventResponse()
            typedResponse.log = eventValues.log
            typedResponse._owner = eventValues.indexedValues[0].value as String
            typedResponse._approved = eventValues.indexedValues[1].value as String
            typedResponse._tokenId = eventValues.indexedValues[2].value as BigInteger
            responses.add(typedResponse)
        }
        return responses
    }

    fun approvalEventFlowable(filter: EthFilter?): Flowable<ApprovalEventResponse> {
        return web3j.ethLogFlowable(filter).map(object : Function<Log?, ApprovalEventResponse> {
            override fun apply(log: Log): ApprovalEventResponse {
                val eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log)
                val typedResponse = ApprovalEventResponse()
                typedResponse.log = log
                typedResponse._owner = eventValues.indexedValues[0].value as String
                typedResponse._approved = eventValues.indexedValues[1].value as String
                typedResponse._tokenId = eventValues.indexedValues[2].value as BigInteger
                return typedResponse
            }
        })
    }

    fun approvalEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<ApprovalEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT))
        return approvalEventFlowable(filter)
    }

    fun getApprovalForAllEvents(transactionReceipt: TransactionReceipt?): List<ApprovalForAllEventResponse> {
        val valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt)
        val responses = ArrayList<ApprovalForAllEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = ApprovalForAllEventResponse()
            typedResponse.log = eventValues.log
            typedResponse._owner = eventValues.indexedValues[0].value as String
            typedResponse._operator = eventValues.indexedValues[1].value as String
            typedResponse._approved = eventValues.nonIndexedValues[0].value as Boolean
            responses.add(typedResponse)
        }
        return responses
    }

    fun approvalForAllEventFlowable(filter: EthFilter?): Flowable<ApprovalForAllEventResponse> {
        return web3j.ethLogFlowable(filter)
            .map(object : Function<Log?, ApprovalForAllEventResponse> {
                override fun apply(log: Log): ApprovalForAllEventResponse {
                    val eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, log)
                    val typedResponse = ApprovalForAllEventResponse()
                    typedResponse.log = log
                    typedResponse._owner = eventValues.indexedValues[0].value as String
                    typedResponse._operator = eventValues.indexedValues[1].value as String
                    typedResponse._approved = eventValues.nonIndexedValues[0].value as Boolean
                    return typedResponse
                }
            })
    }

    fun approvalForAllEventFlowable(
        startBlock: DefaultBlockParameter?,
        endBlock: DefaultBlockParameter?
    ): Flowable<ApprovalForAllEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT))
        return approvalForAllEventFlowable(filter)
    }

    class TransferEventResponse {
        var log: Log? = null
        var _from: String? = null
        var _to: String? = null
        var _tokenId: BigInteger? = null
    }

    class ApprovalEventResponse {
        var log: Log? = null
        var _owner: String? = null
        var _approved: String? = null
        var _tokenId: BigInteger? = null
    }

    class ApprovalForAllEventResponse {
        var log: Log? = null
        var _owner: String? = null
        var _operator: String? = null
        var _approved: Boolean? = null
    }

    companion object {
        private const val BINARY = "Bin file was not provided"
        const val FUNC_GETAPPROVED = "getApproved"
        const val FUNC_APPROVE = "approve"
        const val FUNC_TRANSFERFROM = "transferFrom"
        const val FUNC_SAFETRANSFERFROM = "safeTransferFrom"
        const val FUNC_OWNEROF = "ownerOf"
        const val FUNC_BALANCEOF = "balanceOf"
        const val FUNC_SETAPPROVALFORALL = "setApprovalForAll"
        const val FUNC_ISAPPROVEDFORALL = "isApprovedForAll"
        val TRANSFER_EVENT = Event(
            "Transfer",
            Arrays.asList<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Uint256?>(true) {})
        )
        val APPROVAL_EVENT = Event(
            "Approval",
            Arrays.asList<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Uint256?>(true) {})
        )
        val APPROVALFORALL_EVENT = Event(
            "ApprovalForAll",
            mutableListOf<TypeReference<*>>(
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Address?>(true) {},
                object : TypeReference<Bool?>() {})
        )

        @Deprecated("")
        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            credentials: Credentials?,
            gasPrice: BigInteger?,
            gasLimit: BigInteger?
        ): ERC721 {
            return ERC721(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            gasPrice: BigInteger?,
            gasLimit: BigInteger?
        ): ERC721 {
            return ERC721(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            credentials: Credentials?,
            contractGasProvider: ContractGasProvider?
        ): ERC721 {
            return ERC721(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(
            contractAddress: String?,
            web3j: Web3j?,
            transactionManager: TransactionManager?,
            contractGasProvider: ContractGasProvider?
        ): ERC721 {
            return ERC721(contractAddress, web3j, transactionManager, contractGasProvider)
        }
    }
}