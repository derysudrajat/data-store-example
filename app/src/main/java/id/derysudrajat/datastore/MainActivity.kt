package id.derysudrajat.datastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import id.derysudrajat.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            LocalShared.getCurrentTheme(this@MainActivity)
                .collect {
                    with(binding) {
                        val btnId = getButtonId(it)
                        themeSelection.check(btnId)
                        val textColor = getCurrentTextColor(it)
                        listOf(tvUsername, tvWelcome).forEach { tv ->
                            tv.setTextColor(
                                ContextCompat.getColor(this@MainActivity, textColor)
                            )
                        }

                    }
                    changeTheme(it)
                }
        }
        setContentView(binding.root)

        binding.themeSelection.addOnButtonCheckedListener { group, _, isChecked ->
            if (isChecked) {
                val theme = getChangedTheme(group.checkedButtonId)
                lifecycleScope.launch {
                    LocalShared.changeTheme(this@MainActivity, theme)
                }
            }
        }
    }

    private fun getCurrentTextColor(it: String): Int =
        if (it == Theme.LIGHT) R.color.black else R.color.white

    private fun getChangedTheme(checkedButtonId: Int): String = when (checkedButtonId) {
        binding.btnThemeWhite.id -> Theme.LIGHT
        binding.btnThemeDark.id -> Theme.DARK
        binding.btnThemeDim.id -> Theme.DIM
        else -> Theme.LIGHT
    }

    private fun getButtonId(it: String): Int {
        with(binding) {
            return when (it) {
                Theme.LIGHT -> btnThemeWhite.id
                Theme.DARK -> btnThemeDark.id
                Theme.DIM -> btnThemeDim.id
                else -> btnThemeWhite.id
            }
        }
    }

    private fun changeTheme(theme: String) {
        val currentTheme = when (theme) {
            Theme.DARK -> R.style.DarkTheme
            Theme.LIGHT -> R.style.LightTheme
            Theme.DIM -> R.style.DimTheme
            else -> R.style.LightTheme
        }
        setTheme(currentTheme)
    }
}