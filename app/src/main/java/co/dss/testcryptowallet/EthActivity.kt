package co.dss.testcryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.dss.testcryptowallet.databinding.ActivityEthBinding
import co.dss.testcryptowallet.util.ERC721
import co.dss.testcryptowallet.util.ERC721Metadata
import com.google.protobuf.ByteString
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import wallet.core.java.AnySigner
import wallet.core.jni.*
import wallet.core.jni.proto.Binance
import wallet.core.jni.proto.Cosmos
import wallet.core.jni.proto.Ethereum
import java.math.BigInteger
import kotlin.math.log

class EthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEthBinding

    private lateinit var web3j: Web3j
    private lateinit var fromAddress: String
    private val toAddress = "0xb3934879f73c384d1b8097e3c0bc027a992033bc"
    private val contractAddress = "0xb1a600e16791d3582dc17e32abacdd58b0f2f79e"
    private lateinit var credentials: Credentials
    private lateinit var token: ERC721

    private val urlKovanApi = "https://kovan.infura.io/v3/064a95b43a65465a9de6f80399df4d94"
    private val urlRopstenApi = "https://ropsten.infura.io/v3/064a95b43a65465a9de6f80399df4d94"

    private lateinit var wallet: HDWallet

    init {
        System.loadLibrary("TrustWalletCore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEthBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setUpWeb3j()
//
//        setTokenNft()
//
//        connectEthereumNetWork()
//
//        getErc721Metadata()
//
//        sendERC721()

        wallet = HDWallet(
            "grow casual inherit run grant circle debate quit planet tide blanket only",
            ""
        )

        generateAddressTrustWalletCore()

        sendBNB()


    }

    private fun setUpWeb3j() {
        web3j =
            Web3j.build(HttpService(urlRopstenApi))

        credentials = Credentials.create(generateWallet())

        fromAddress = credentials.address
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

    private fun setTokenNft() {
        token = ERC721.load(contractAddress, web3j, credentials, DefaultGasProvider())
    }

    private fun connectEthereumNetWork() {
        binding.btnConnect.setOnClickListener {
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
    }

    private fun getErc721Metadata() {
        binding.btnGetErc721Metadata.setOnClickListener {
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

    private fun generateAddressTrustWalletCore() {

        binding.btnGenerateAddressTrustWalletCore.setOnClickListener {
            val key = wallet.getKey(CoinType.ETHEREUM, "m/44'/60'/0'/0/0")

            val addressETH = CoinType.ETHEREUM.deriveAddress(key)

            Log.d("log_test", "generateAddressTrustWalletCore: address $addressETH ")
        }

    }

    private fun sendBNB() {
        val addressBinance = wallet.getAddressForCoin(CoinType.BINANCE)
        val privateKeyBinance = wallet.getKeyForCoin(CoinType.BINANCE)
        val privateKey = PrivateKey(privateKeyBinance)

        val token = Binance.SendOrder.Token.newBuilder().apply {
            denom = "BNB"
            amount = 0
        }

        val orderInput = Binance.SendOrder.Input.newBuilder().apply {
            address = ByteString.copyFrom(AnyAddress(addressBinance, CoinType.BINANCE).data())
            addCoins(token)
        }

        val orderOutput = Binance.SendOrder.Output.newBuilder().apply {
            address = ByteString.copyFrom(
                AnyAddress(
                    "bnb1hlly02l6ahjsgxw9wlcswnlwdhg4xhx38yxpd5",
                    CoinType.BINANCE
                ).data()
            )
            addCoins(token)
        }

        val input = Binance.SigningInput.newBuilder().apply {
            chainId = "Binance-Chain_Nile"
            accountNumber = 0
            sequence = 0
            source = 0
            setPrivateKey(ByteString.copyFrom(privateKey.data()))
            memo = ""
            setSendOrder(Binance.SendOrder.newBuilder().apply {
                addInputs(orderInput)
                addOutputs(orderOutput)
            })
        }.build()

        val outPut = AnySigner.sign(input, CoinType.BINANCE, Binance.SigningOutput.parser())
        Log.d("log_test", "sendBNB: ${outPut.encoded}")
    }
}