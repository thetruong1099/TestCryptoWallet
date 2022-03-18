package co.dss.testcryptowallet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.dss.testcryptowallet.databinding.ActivityMainBinding
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.Transfer
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider
import org.web3j.utils.Convert
import wallet.core.jni.CoinType
import wallet.core.jni.HDVersion
import wallet.core.jni.HDWallet
import java.math.BigInteger


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var web3j: Web3j
    private val fromAddress = "0xaa437fb6af74febefc2fffa4fbbbe38605b752d7"
    private val toAddress = "0xb3934879f73c384d1b8097e3c0bc027a992033bc"
    private val contractAddress = "0x52BF7E3dF46e40d1D0abA5e97e1495DE278CA0a4"
    private lateinit var credentials: Credentials
    private lateinit var token: ERC721

    private val urlKovanApi = "https://kovan.infura.io/v3/064a95b43a65465a9de6f80399df4d94"
    private val urlRopstenApi = "https://ropsten.infura.io/v3/064a95b43a65465a9de6f80399df4d94"

    init {
        System.loadLibrary("TrustWalletCore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        web3j =
            Web3j.build(HttpService(urlRopstenApi))

        credentials = Credentials.create(generateWallet())

        val gasPrice = web3j.ethGasPrice().sendAsync().get().gasPrice

        val gasLimit =
            web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).sendAsync()
                .get().block.gasLimit

        val gasCost = gasLimit * gasPrice

        val balance = web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST).sendAsync()
            .get().balance



        Log.d(
            "log_test",
            "onCreate: balance ${
                Convert.fromWei(
                    balance.toString(),
                    Convert.Unit.ETHER
                )
            } estimateGas ${Transfer.GAS_LIMIT}  $gasLimit   ${
                Convert.fromWei(
                    gasCost.toString(),
                    Convert.Unit.ETHER
                )
            }"
        )

        token = ERC721.load(
            contractAddress, web3j, credentials, StaticGasProvider(
                gasPrice,
                Transfer.GAS_LIMIT
            )
        )
        binding.btnConnect.setOnClickListener {
            connectEthereumNetWork()
        }

        binding.btnGetBalanceERC721.setOnClickListener {
            getBalanceERC721FromOwner()
        }


        binding.btnSend.setOnClickListener {
//            sendErc20()
            sendERC721()
//            val txtHash = safeTransferFrom()
//            Log.d("log_test", "onCreate: $txtHash")
//            getInformation()
        }

        binding.btnOwnerOfNft.setOnClickListener {
            getOwnerNft()
        }

//        generateWallet()

        binding.btnGenerateMultiCoin.setOnClickListener {
            testTrustWallet()
        }

        binding.btnErc721Metadata.setOnClickListener {
            getErc721Metadata()
        }
    }

    private fun generateWallet(): Bip32ECKeyPair {
        val mnemonic = "grow casual inherit run grant circle debate quit planet tide blanket only"

        val masterKeypair =
            Bip32ECKeyPair.generateKeyPair(
                MnemonicUtils.generateSeed(
                    mnemonic,
                    null
                )
            )
        val path =
            intArrayOf(
                44 or Bip32ECKeyPair.HARDENED_BIT,
                0 or Bip32ECKeyPair.HARDENED_BIT,
                0 or Bip32ECKeyPair.HARDENED_BIT,
                0,
                0
            )
        return Bip32ECKeyPair.deriveKeyPair(masterKeypair, path)
    }

    private fun connectEthereumNetWork() {
        try {
            //if the client version has an error the user will not gain access if successful the user will get connected
            val clientVersion: Web3ClientVersion? =
                web3j.web3ClientVersion()?.sendAsync()?.get()
            if (!clientVersion?.hasError()!!) {
                Log.d("log_test", "connectEthereumNetWork: ok")
            } else {
                Log.d("log_test", "connectEthereumNetWork: fail ${clientVersion.error.message}")
            }
        } catch (e: Exception) {
            Log.d("log_test", "connectEthereumNetWork: fail ${e.message}")
        }
    }

    private fun getBalanceERC721FromOwner() {
        try {
            val balance = token.balanceOf(fromAddress).sendAsync().get()
            Log.d("log_test", "getBalanceERC721: $balance")
        } catch (e: Exception) {
            Log.d("log_test", "getBalanceERC721: ${e.message}")
        }

    }

    private fun sendERC721() {
        val tokenId = BigInteger("2", 16)
        val value = BigInteger.valueOf(1)
        try {
            val transfer = token.transferFrom(
                fromAddress,
                toAddress,
                tokenId,
                value
            ).sendAsync()
            Log.d("log_test", "sendERC721: transfer status ${transfer.get()}")
        } catch (e: Exception) {
            Log.d("log_test", "sendERC721: exception ${e.message}")
        }
    }

    private fun getOwnerNft() {
        val tokenId = BigInteger("0", 16)
        try {
            val owner = token.ownerOf(tokenId).sendAsync().get()
            Log.d("log_test", "getOwnerNft: $owner")
        } catch (e: Exception) {
            Log.d("log_test", "getOwnerNft: exception ${e.message}")
        }
    }

/*    private fun encodeTransferERC20Data(
        fromAddress: String,
        toAddress: String,
        sum: BigInteger
    ): String {
        val function = Function(
            "TransferERC20",
            listOf(
                Address(fromAddress),
                Address(toAddress),
                Uint256(sum)
            ),
            listOf(object : org.web3j.abi.TypeReference<Bool>() {})
        )
        return FunctionEncoder.encode(function)
    }*/

/*    private fun sendErc20() {
        try {
            val manager = RawTransactionManager(web3j, credentials)
            val fromAddress = "0x29c0c2bEa26708282Aed3a87379A03cfc41624c4"
            val toAddress = "0xB3934879F73c384d1b8097e3C0BC027A992033BC"
            val contractAddress = "0x2c9e00EeEcf8a6693AFae621557f3b5284D141B6"
            val sum = BigInteger.valueOf(100)
            val data = encodeTransferERC20Data(fromAddress, toAddress, sum)
            val gasPrice = web3j.ethGasPrice().sendAsync().get().gasPrice
            Log.d("log_test", "sendErc20: gas price $gasPrice")
            val gasLimit = BigInteger.valueOf(12000)
            val transaction =
                manager.sendTransaction(gasPrice, gasLimit, contractAddress, data, null)
            Log.d("log_test", "sendErc20: transaction ${transaction.transactionHash}")
        } catch (e: Exception) {
            Log.d("log_test", "sendErc20: exception ${e.message} ")
        }
    }*/

/*    @Throws(Exception::class)
    fun safeTransferFrom(): String? {
        //getting Null pointer at SEND.
        val setApproval: TransactionReceipt = token.setApprovalForAll(fromAddress, true).send()
        val approvalHash = setApproval.transactionHash
        Log.d("log_test", "safeTransferFrom: Approval Status Hash >>>>> $approvalHash")

        getting Null pointer at SEND.
        val sendToken: TransactionReceipt =
            token.safeTransferFrom(fromAddress, toAddress, token1, data, value).send()
        val txHash = sendToken.transactionHash

        return if (txHash == null) {
            val error = "Not sent"
            Log.d("log_test", "safeTransferFrom: $error")
            error
        } else {
            Log.d("log_test", "safeTransferFrom: tx Hash >>>>>>> $txHash")
            val receiptProcessor: TransactionReceiptProcessor = PollingTransactionReceiptProcessor(
                web3j,
                TransactionManager.DEFAULT_POLLING_FREQUENCY,
                TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
            )
            val txReceipt = receiptProcessor.waitForTransactionReceipt(txHash)
            Log.d("log_test", "safeTransferFrom: Transaction Receipt >>>>> $txReceipt")
            txHash
        }
    }*/

/*    private fun getInformation() {
        val fromAddress = "0xb3934879f73c384d1b8097e3c0bc027a992033bc"
        val toAddress = "0xaa437fb6af74febefc2fffa4fbbbe38605b752d7"
        val value: BigInteger = BigInteger.valueOf(1)
        val web3j =
            Web3j.build(HttpService("https://ropsten.infura.io/v3/064a95b43a65465a9de6f80399df4d94"))
        val contractAddress = "0x52BF7E3dF46e40d1D0abA5e97e1495DE278CA0a4"
        val tokenId = "0"
        val data = ByteArray(0)
        val token1 = BigInteger(tokenId, 16)
        val credentials = Credentials.create(generateWallet())
        val token = ERC721.load(contractAddress, web3j, credentials, DefaultGasProvider())

        try {
            val balance = token.balanceOf(fromAddress).send()
            Log.d(
                "log_test",
                "getInformation: ${
                    web3j.ethGetBalance(
                        fromAddress,
                        DefaultBlockParameterName.LATEST
                    ).sendAsync().get().balance
                }"
            )
        } catch (e: Exception) {
            Log.d("log_test", "getInformation: mess ${e.message}")
        }

    }*/

    private fun testTrustWallet() {
        val wallet = HDWallet(
            "grow casual inherit run grant circle debate quit planet tide blanket only",
            ""
        )
        val addressETH = wallet.getAddressForCoin(CoinType.ETHEREUM)
        Log.d("log_test", "testTrustWallet: add eth $addressETH")
        val addressBNB = wallet.getAddressForCoin(CoinType.BINANCE)
        Log.d("log_test", "testTrustWallet: add bnb $addressBNB")
    }

    private fun getErc721Metadata() {
        val erc721Metadata =
            ERC721Metadata.load(contractAddress, web3j, credentials, DefaultGasProvider())

        val name = erc721Metadata.name().sendAsync().get()

        Log.d("log_test", "getErc721Metadata: name $name ")

        val symbols = erc721Metadata.symbol().sendAsync().get()

        Log.d("log_test", "getErc721Metadata: symbol $symbols")

        val tokenId = BigInteger("2", 16)

        val tokenUri = erc721Metadata.tokenURI(tokenId).sendAsync().get()

        Log.d("log_test", "getErc721Metadata: tokenUri $tokenUri")
    }

}