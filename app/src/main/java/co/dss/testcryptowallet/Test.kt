package co.dss.testcryptowallet

/*
import android.util.Log
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
import wallet.core.jni.AnyAddress
import wallet.core.jni.Base58
import wallet.core.jni.CoinType
import wallet.core.jni.PrivateKey
import wallet.core.jni.proto.Binance
import java.math.BigInteger

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
    val transaction = outPut.encoded.toByteArray().toHex()
    Log.d("log_test", "sendBNB: ${transaction}")
}

fun ByteArray.toHex(): String =
    joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

private fun generateAddressNear() {
    val prk = wallet.getKeyForCoin(CoinType.NEAR)
    val privateKey = PrivateKey(prk)

    Log.d(
        "log_test",
        "generateAddressNear: private key hex string:  ${ByteString.copyFrom(privateKey.data())}}"
    )

    val address = wallet.getAddressForCoin(CoinType.NEAR)

    val keyBase58 =
        Base58.decodeNoCheck("5acCypeqY1sZRVcKhy9nBwFtVm3B92tHMkKFGapNSpL2vVYHhw6D26g4icaMKVttz4nFBe8iNagWFxe7eagvWtM4")
            .sliceArray(0..31)
    val key = PrivateKey(keyBase58)

    Log.d(
        "log_test",
        "generateAddressNear: key base58:  ${ByteString.copyFrom(keyBase58)}"
    )

//        val prkEncode = Base58.encodeNoCheck(privateKey.data().sliceArray(0..31))
//
//        Log.d(
//            "log_test",
//            "generateAddressNear: private key encode:  ${prkEncode}"
//        )
//
//        //GR7YnKdGvVfA5idp6RwDyvSwvCwFb2BiiV7HAQNmNghf
//        //salute bench another truly convince immense opinion angle logic relief truck original
//
//        val keyBase582 =
//            Base58.decodeNoCheck("GR7YnKdGvVfA5idp6RwDyvSwvCwFb2BiiV7HAQNmNghf")
//                .sliceArray(0..31)
//
//        Log.d(
//            "log_test",
//            "generateAddressNear: key base58 2:  ${keyBase582.toString()}"
//        )
}

fun testTransferSign() {
//        val transferAction = NEAR.Transfer.newBuilder().apply {
//            deposit = ByteString.copyFrom("01000000000000000000000000000000".toHexByteArray())
//        }.build()
//        val signingInput = NEAR.SigningInput.newBuilder().apply {
//            signerId = "test.near"
//            nonce = 1
//            receiverId = "whatever.near"
//            addActionsBuilder().apply {
//                transfer = transferAction
//            }
//            blockHash = ByteString.copyFrom(Base58.decodeNoCheck("244ZQ9cgj3CQ6bWBdytfrJMuMQ1jdXLFGnr4HhvtCTnM"))
//            privateKey = ByteString.copyFrom(Base58.decodeNoCheck("3hoMW1HvnRLSFCLZnvPzWeoGwtdHzke34B2cTHM8rhcbG3TbuLKtShTv3DvyejnXKXKBiV7YPkLeqUHN1ghnqpFv").sliceArray(0..31))
//        }.build()
//
//        val output = AnySigner.sign(signingInput, CoinType.NEAR, NEAR.SigningOutput.parser())

}*/
