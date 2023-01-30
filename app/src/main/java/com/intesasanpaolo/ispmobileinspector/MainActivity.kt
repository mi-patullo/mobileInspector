package com.intesasanpaolo.ispmobileinspector

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.intesasanpaolo.ispmobileinspector.databinding.ActivityMainBinding
import com.intesasanpaolo.ispmobileinspector.ui.*
import com.intesasanpaolo.ispmobileinspector.ui.dashboard.DashboardFragment
import com.intesasanpaolo.ispmobileinspector.ui.home.HomeFragment
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class MainActivity : AppCompatActivity(), Listener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startFirstFragment()

        binding.constraint.visibility = View.VISIBLE

        binding.imageRawLog.setOnClickListener {
            startRawFragment()
        }

        binding.imageBackend.setOnClickListener {
            startHomeFragment()
        }

        /*binding.rawlogButton.setOnClickListener {
            startRawFragment()
        }

        binding.backendButton.setOnClickListener {
            startHomeFragment()
        }*/
    }

    fun startNewActivity(intent: Intent, enableActivityTransition: Boolean) {
        val transition = ActivityOptions.makeSceneTransitionAnimation(this)

        if (enableActivityTransition)
            startActivity(intent, transition.toBundle())
        else
            startNewActivity(intent)
    }

    fun startNewActivity(intent: Intent) {
        startActivity(intent)
    }

    private fun getSsoMainIntent(context: Context): Intent {
        val pm = context.packageManager
        return pm.getLaunchIntentForPackage("com.latuabancaperandroid") ?: Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "market://details?id=com.intesasanpaolo.insurance"
            ))
    }

    private fun startFirstFragment() {
        addFragment(
            R.id.cj_cui_container,
            HomeFragment.newInstance(),
            HomeFragment.TAG
        )
    }

    private fun startRawFragment() {
        replaceFragment(
            R.id.cj_cui_container,
            DashboardFragment.newInstance(),
            DashboardFragment.TAG
        )
    }

    private fun startHomeFragment() {
        replaceFragment(
            R.id.cj_cui_container,
            HomeFragment.newInstance(),
            HomeFragment.TAG
        )
    }

    fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.inTransaction {
            add(frameId, fragment, tag)
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    fun AppCompatActivity.replaceFragment(
        frameId: Int,
        fragment: Fragment,
        tag: String,
        addToBackStack: Boolean = false,
    ) {
        supportFragmentManager.inTransaction {
            replace(frameId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            } else this
        }
    }

    override fun goToDettaglio(card: CardRawLog) {
        binding.constraint.visibility = View.VISIBLE

        replaceFragment(
            R.id.cj_cui_container,
            DettaglioFragment.newInstance(card),
            DettaglioFragment.TAG
        )
    }

    override fun returnToHome() {
        binding.constraint.visibility = View.VISIBLE
        replaceFragment(
            R.id.cj_cui_container,
            DashboardFragment.newInstance(),
            DashboardFragment.TAG
        )
    }

    override fun goToSearch() {
        binding.constraint.visibility = View.GONE
        replaceFragment(
            R.id.cj_cui_container,
            SearchFragment.newInstance(),
            SearchFragment.TAG
        )
    }

   /* @Throws(FileNotFoundException::class)
     fun openAssetFile(uri: Uri, mode: String?): AssetFileDescriptor? {
        val am: AssetManager = assets
        val file_name = uri.lastPathSegment ?: throw FileNotFoundException()
        var afd: AssetFileDescriptor? = null
        try {
            afd = am.openFd(file_name)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return afd
    }*/

    override fun hh() {
        try {
            val file = File("/Users/m.patullo/AndroidStudioProjects/ISPMobileInspector/app/src/main/assets/CardRawLog.json")
                val uri =
                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
                val intent = Intent(Intent.ACTION_SEND)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.type = "*/*"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            //toast("Error")
        }
    }

    override fun shareTransferDetails() {
        val transferData =  "file di prova"
        transferData?.let { data ->
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, data)
            intent.putExtra(Intent.EXTRA_SUBJECT,
                "Secondo teto di prova")
            startActivity(Intent.createChooser(intent,
                "terzo testo di prova"))
        }
    }
}

interface Listener {
    fun goToDettaglio(card: CardRawLog)
    fun returnToHome()
    fun goToSearch()
    fun shareTransferDetails()
    fun hh()
}