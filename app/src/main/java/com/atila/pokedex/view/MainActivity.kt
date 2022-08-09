package com.atila.pokedex.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.atila.pokedex.R
import com.atila.pokedex.databinding.ActivityMainBinding
import com.atila.pokedex.viewmodel.FavoritePokemonsViewModel
import com.google.android.material.badge.BadgeDrawable

class MainActivity : AppCompatActivity() {

    //binding on activities
    private lateinit var binding: ActivityMainBinding

    // viewModel declaration
    private val viewModel: FavoritePokemonsViewModel by viewModels()

    private lateinit var badge: BadgeDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //bottomNavbar (menu -> navigation_bar.xml item id's must be same with fragment names)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        setupWithNavController(binding.bottomNavigation, navController.navController)

        badge = binding.bottomNavigation.getOrCreateBadge(R.id.favoritePokemonList2)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set:
        badge.number = 0

        // to set the favorite badge count on the launch of the application
        viewModel.refreshFavoritePokemonCount()
        observeLiveData()

        supportActionBar?.hide()
    }

    private fun observeLiveData(){
        viewModel.favoritePokemonCount.observeForever(Observer {
                count: Int ->
            // Perform an action with the latest item data
            count.let {
                badge.number = count
                println("mainactivity observelivedata çağırıldı badge number -> "+ badge.number)
            }
        })

    }

    fun refreshBadgeCount(favoriteCount : Int){
        badge.number = favoriteCount
        println("refreshbadgecount çağırıldı -> favorite count -> $favoriteCount")
    }

}

