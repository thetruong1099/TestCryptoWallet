package co.dss.testcryptowallet

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.dss.testcryptowallet.databinding.ActivityBscBinding
import co.dss.testcryptowallet.util.ERC20
import org.web3j.crypto.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigInteger

class BSCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBscBinding

    private lateinit var web3j: Web3j
    private lateinit var credentials: Credentials
    private lateinit var fromAddress: String

    private val toAddress = "0xAa437FB6Af74feBEfC2FFfa4FBBbe38605B752d7"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBscBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpWeb3j()
        getBSCBalance()
        sendBSC()
    }

    private fun setUpWeb3j() {
        web3j =
            Web3j.build(HttpService("https://data-seed-prebsc-1-s1.binance.org:8545"))

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


    @SuppressLint("CheckResult")
    private fun getBSCBalance() {
        binding.btnGetBalance.setOnClickListener {
            val balance = web3j.ethGetBalance(
                "0x29c0c2bEa26708282Aed3a87379A03cfc41624c4",
                DefaultBlockParameterName.LATEST
            ).sendAsync().get().balance
            Log.d("log_test", "getBSCBalance: $balance")

/*            val binanceManager = BinanceManager.getInstance()
            binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545")

            val context = this@BSCActivity.baseContext

            binanceManager.getBNBBalance("0x29c0c2bEa26708282Aed3a87379A03cfc41624c4", context)
                .subscribe { balance ->
                    Log.d("log_test", "getBSCBalance: $balance")
                }

            binanceManager.sendBNB("")*/
        }
    }

    private fun sendBSC() {
        binding.btnSendBSC.setOnClickListener {
            try {
                val nonce =
                    web3j.ethGetTransactionCount(
                        credentials.address,
                        DefaultBlockParameterName.PENDING
                    ).sendAsync().get().transactionCount

                val weiValue = Convert.toWei("1", Convert.Unit.ETHER)

                val gasPrice = web3j.ethGasPrice().sendAsync().get().gasPrice
//                val txManager = FastRawTransactionManager(web3j, credentials, 97L)

                val rawTransaction = RawTransaction.createEtherTransaction(
                    nonce,
                    gasPrice,
                    BigInteger.valueOf(4300000),
                    "0xAa437FB6Af74feBEfC2FFfa4FBBbe38605B752d7",
                    weiValue.toBigIntegerExact()
                )

                val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue = Numeric.toHexString(signedMessage)
                val response = web3j.ethSendRawTransaction(hexValue).sendAsync().get()
                Log.d("log_test", "sendBSC: transfer ${response.transactionHash}")
            } catch (e: Exception) {
                Log.d("log_test", "sendBSC: exception ${e.message}")
            }

        }
    }
}