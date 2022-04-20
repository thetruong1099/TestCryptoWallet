package co.dss.testcryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.dss.testcryptowallet.databinding.ActivityEthBinding
import co.dss.testcryptowallet.util.ERC721
import co.dss.testcryptowallet.util.ERC721Metadata
import com.google.protobuf.ByteString
import org.bouncycastle.util.encoders.Base64
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
import wallet.core.jni.proto.Ethereum
import wallet.core.jni.proto.NEAR
import java.math.BigInteger
import kotlin.math.log

class EthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEthBinding

    private val urlKovanApi = "https://kovan.infura.io/v3/064a95b43a65465a9de6f80399df4d94"
    private val urlRopstenApi = "https://ropsten.infura.io/v3/064a95b43a65465a9de6f80399df4d94"

    private lateinit var wallet: HDWallet

    private lateinit var address: String

    init {
        System.loadLibrary("TrustWalletCore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wallet = HDWallet(
            "grow casual inherit run grant circle debate quit planet tide blanket only",
            ""
        )

        binding.btnGetAddress.setOnClickListener {
            getAddress()
        }

        binding.btnSendEth.setOnClickListener {
            transactionSigning()
        }

    }

    private fun getAddress() {

        address = wallet.getAddressForCoin(CoinType.ETHEREUM)
        Log.d("log_test", "getAddress: $address")
    }

    private fun transactionSigning() {
        val key = wallet.getKeyForCoin(CoinType.ETHEREUM)

        val signInput = Ethereum.SigningInput.newBuilder().apply {
            privateKey = ByteString.copyFrom(PrivateKey(key).data())
            toAddress = "0xAa437FB6Af74feBEfC2FFfa4FBBbe38605B752d7"
            chainId = ByteString.copyFrom(BigInteger("03").toByteArray())

        }
    }
}