package co.dss.testcryptowallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import co.dss.testcryptowallet.databinding.ActivityMainBinding
import co.dss.testcryptowallet.util.toHex
import co.dss.testcryptowallet.util.toHexByteArray
import org.komputing.khash.sha256.extensions.sha256
import wallet.core.jni.Base58
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.PrivateKey
import java.nio.ByteBuffer
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    init {
        System.loadLibrary("TrustWalletCore")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startEthActivity()
        startBSCActivity()

        binding.btnTest.setOnClickListener {
//            testPrivateKeyBtc()
            testPrivateKeySOL()
        }
    }

    private fun startEthActivity() {
        binding.btnEth.setOnClickListener {
            val intent = Intent(this, EthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startBSCActivity() {
        binding.btnBSC.setOnClickListener {
            val intent = Intent(this, BSCActivity::class.java)
            startActivity(intent)
        }
    }

    private fun testPrivateKeyBtc() {
/*
        val wallet = HDWallet(
            "grow casual inherit run grant circle debate quit planet tide blanket only",
            ""
        )

        val account = wallet.getAddressForCoin(CoinType.BITCOIN)

        Log.d("walletTest", "testPrivateKey account: ${account}")

        val privateKey = wallet.getKeyForCoin(CoinType.BITCOIN)

        val privateKeyHex = privateKey.data().toHex()

        val privateKeyWIFHex = "80${privateKeyHex}01"

        Log.d(
            "walletTest",
            "testPrivateKey private key: ${Base58.encode(privateKeyWIFHex.toHexByteArray())}"
        )

        val privateKeyByteArray = privateKeyWIFHex.toHexByteArray().sha256().toHex()

//        val privKeyByteArray = privKeyWIFHex.toHexByteArray()
*/


        val priv = Base58.decode("KzWW1aMV394YDBymPUZenp6uAfsXKGHMjqGgvuG14GAMf7i1JaYv").toHex()
        Log.d("walletTest", "testPrivateKey private key: ${priv}")

        val privateKeyHex = priv.slice(2..priv.length - 3)
        Log.d("walletTest", "testPrivateKey private key hex: ${privateKeyHex}")

        val invalid = PrivateKey.isValid(privateKeyHex.toHexByteArray(), CoinType.BITCOIN.curve())

        Log.d("walletTest", "testPrivateKey invalid: ${invalid}")
    }

    private fun testPrivateKeySOL() {
/*        val wallet = HDWallet(
            "cloth upper tobacco vital vacuum outer dry spoon pledge village stone pave",
            ""
        )

        val account = wallet.getAddressForCoin(CoinType.SOLANA)

        Log.d("walletTest", "testPrivateKey account: ${account}")

        val privateKey = wallet.getKeyForCoin(CoinType.SOLANA)

        val privateKeyHex = privateKey.data().toHex()

        val publickey = privateKey.publicKeyEd25519

        val publickeyHex = publickey.data().toHex()

        val privateKeyWIFHex = privateKeyHex + publickeyHex

        val codeBase58 = Base58.encodeNoCheck(privateKeyWIFHex.toHexByteArray())

        Log.d(
            "walletTest",
            "testPrivateKey private key: ${codeBase58}"
        )*/

        val codebase58 =
            Base58.decodeNoCheck("AFL4ZSeD3dmzESYBMX7HSGDZryKUtNMqJWgYDnxpSm3FadsPeqPFSe7w4mz8LhDLGx1BKPjDsGaSARL6B9jfR7v")

        val privateKeyWIFHex = codebase58.toHex()

        val privateKeyHex = privateKeyWIFHex.slice(0..63)

        val valid = PrivateKey.isValid(privateKeyHex.toHexByteArray(), CoinType.SOLANA.curve())

        Log.d(
            "walletTest",
            "testPrivateKey private key: ${valid}"
        )
    }

}