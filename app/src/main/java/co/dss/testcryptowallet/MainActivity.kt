package co.dss.testcryptowallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.dss.testcryptowallet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startEthActivity()
        startBSCActivity()
    }

    private fun startEthActivity() {
        binding.btnEth.setOnClickListener {
            val intent = Intent(this, EthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startBSCActivity(){
        binding.btnBSC.setOnClickListener {
            val intent = Intent(this, BSCActivity::class.java)
            startActivity(intent)
        }
    }
}