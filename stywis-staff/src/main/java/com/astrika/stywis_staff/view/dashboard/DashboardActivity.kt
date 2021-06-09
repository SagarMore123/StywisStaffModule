package com.astrika.stywis_staff.view.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.astrika.stywis_staff.BuildConfig
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.DashboardDrawerAdapter
import com.astrika.stywis_staff.adapters.DashboardSubMenuAdapter
import com.astrika.stywis_staff.databinding.ActivityDashboardBinding
import com.astrika.stywis_staff.master_controller.sync.MasterSyncIntentService
import com.astrika.stywis_staff.models.DashboardDrawerDTO
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.viewmodels.DashboardViewModel
import com.astrika.stywis_staff.view.profile.UpdateProfile
import kotlinx.android.synthetic.main.app_bar_dashboard.*


class DashboardActivity : AppCompatActivity(), DashboardDrawerAdapter.OnItemClickListener,
    DashboardSubMenuAdapter.OnSubMenuItemClickListener, UpdateProfile {

    lateinit var binding: ActivityDashboardBinding
    lateinit var navController: NavController
    private var progressBar = CustomProgressBar()

    private val requestExternalStorage = 1
    private val permissionsStorage =
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private var locationActivity = this
    private lateinit var viewModel: DashboardViewModel
    private lateinit var dashboardDrawerAdapter: DashboardDrawerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dashboard)
//        verifyStoragePermissions(this)
//        setChildActivity(locationActivity)
        val sharedPreferences = Constants.getSharedPreferences(application)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        viewModel = Utils.obtainBaseObservable(
            this,
            DashboardViewModel::class.java,
            this,
            binding.root
        )!!
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.navigationMenu.versionNameTxt.text = "Version " + BuildConfig.VERSION_NAME + " "

//        Log.e("Crispminds Psw",Constants.passwordEncrypt("Nitinz@123"))

        binding.navigationMenu.closeDrawerImg.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        dashboardDrawerAdapter = DashboardDrawerAdapter(this, this, this)
        binding.navigationMenu.dashboardMenuRecyclerView.adapter = dashboardDrawerAdapter

        viewModel.fetchDrawerMenu()
        initNavigationDrawer()

        binding.navigationMenu.profileImage.setOnClickListener { // Drawer Profile Image
            binding.content.profileImage.performClick()
        }
        binding.content.profileImage.setOnClickListener { // Dashboard Toolbar Profile Image
            navController.navigate(R.id.profileFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

//        onClickListeners()
        observers()
//        startImmediateSync(this)
//        Utils.showChooseProfileDialog(this)

        if (!Constants.decrypt(sharedPreferences.getString(Constants.PROFILE_IMAGE_PATH, ""))
                .isNullOrBlank()
        ) {
            viewModel.profilePathObservableField.set(
                Constants.decrypt(
                    sharedPreferences.getString(
                        Constants.PROFILE_IMAGE_PATH,
                        ""
                    )
                )
            )
        } else {
            binding.navigationMenu.profileImage.setImageURI(Constants.PROFILE_IMAGE_URI) // Drawer Profile Image
            binding.content.profileImage.setImageURI(Constants.PROFILE_IMAGE_URI) // Dashboard Toolbar Profile Image
        }

    }

    private fun observers() {

        viewModel.dashboardDrawerListMutableLiveData.observe(this, Observer {
            dashboardDrawerAdapter.setDrawerDashboardList(it)
        })

        viewModel.showProgressBar.observe(this, Observer {
            if (it)
                progressBar.show(this, "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.isConfirm.observe(this, Observer {
            if (it) {
                viewModel.logoutUser()
            }

        })

    }

    /*private fun onClickListeners() {

        binding.navigationMenu.infoManagement.setOnClickListener {
            if (binding.navigationMenu.infoManagementLayout.visibility == View.VISIBLE) {
                binding.navigationMenu.infoManagementLayout.visibility = View.GONE
                binding.navigationMenu.infoMgmtDropdown.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_arrow_down
                    )
                )
            } else {
                binding.navigationMenu.infoManagementLayout.visibility = View.VISIBLE
                binding.navigationMenu.infoMgmtDropdown.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_arrow_up
                    )
                )
            }
        }

        basicInfoLayout.setOnClickListener {
            sub_title_txt.text = basicInfoTitleTxt.text
            navController.navigate(R.id.BasicInfoFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        timingsInfoLayout.setOnClickListener {
            sub_title_txt.text = timingsInfoTitleTxt.text
            navController.navigate(R.id.PostLoginTimingsFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        communicationInfoLayout.setOnClickListener {
            sub_title_txt.text = communicationInfoTitleTxt.text
            navController.navigate(R.id.PostLoginAddressFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        galleryInfoLayout.setOnClickListener {
            sub_title_txt.text = galleryInfoTitleTxt.text
            navController.navigate(R.id.galleryImagesFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        menuInfoLayout.setOnClickListener {
            sub_title_txt.text = menuInfoTitleTxt.text
            navController.navigate(R.id.menuImagesFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        subAdminManagementLayout.setOnClickListener {
            sub_title_txt.text = subAdminManagementTitleTxt.text
            navController.navigate(R.id.subAdminFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        discount_management_layout.setOnClickListener {
            Constants.changeActivity<DiscountManagementActivity>(this)
            finish()
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        menuConfiguration.setOnClickListener {
            if (menu_configuration_options.visibility == View.VISIBLE) {
                menu_configuration_options.visibility = View.GONE
                binding.navigationMenu.menuConfigurationDropdown.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_arrow_down
                    )
                )
            } else {
                menu_configuration_options.visibility = View.VISIBLE
                binding.navigationMenu.menuConfigurationDropdown.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_arrow_up
                    )
                )
            }
        }
        menu_category_layout.setOnClickListener {
            sub_title_txt.text = menuCategoryTitleTxt.text
            navController.navigate(R.id.menuCategoryFragment)
//            navController.navigate(R.id.menuSectionFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        menu_section_layout.setOnClickListener {
            sub_title_txt.text = menuSectionTitleTxt.text
            navController.navigate(R.id.menuSectionFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        dish_category_layout.setOnClickListener {
            sub_title_txt.text = dishesTitleTxt.text
            navController.navigate(R.id.dishCategoryFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        staff_management_layout.setOnClickListener {
            sub_title_txt.text = "Staff User Management"
            navController.navigate(R.id.staffUserFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

    }
    }*/

    private fun initNavigationDrawer() {
        setSupportActionBar(binding.content.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.content.toolbar.setTitleTextAppearance(this, R.style.AirBnbFontTextAppearance)
//        binding.content.toolbar.setTitleTextColor(applicationContext.resources.getColor(R.color.colorWhite, null))
        binding.content.toolbar.setupWithNavController(navController, appBarConfiguration)
        setSupportActionBar(findViewById(R.id.toolbar))


//        navController = Navigation.findNavController(this,R.id.navigation_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        val toggle =
            ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.content.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_drawer)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.toolbarNavigationClickListener = View.OnClickListener {

            if (navController.currentDestination?.id == R.id.DashboardFragment) {

                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }

            } else {
                navController.navigateUp()
            }


            /*if(navController.currentDestination?.id == R.id.searchGamesFragment){
                        navController.navigateUp()
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }*/
        }


    }

    private fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                permissionsStorage,
                requestExternalStorage
            )
        }
    }


/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        when (requestCode) {
            requestExternalStorage -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isEmpty()
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        this,
                        "Cannot write images to external storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

*/


    private fun startImmediateSync(context: Context) {
        val intentToSyncImmediately = Intent(context, MasterSyncIntentService::class.java)
        context.startService(intentToSyncImmediately)
    }

    override fun onSubMenuItemClick(
        mainContainerPosition: Int,
        position: Int,
        dashboardDrawerDTO: DashboardDrawerDTO
    ) {

        if (!dashboardDrawerDTO.name.isBlank()) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            when (dashboardDrawerDTO.name) {
                Constants.OUTLET_DASHBOARD -> {
                    navController.navigate(R.id.DashboardFragment)
                }
                Constants.TAKE_ORDER -> {
                    sub_title_txt.text = "Take Order"
                    navController.navigate(R.id.MenuFragment)
                }
                Constants.STOCK_MANAGEMENT -> {
                    sub_title_txt.text = Constants.STOCK_MANAGEMENT
                    navController.navigate(R.id.stockFragment)
                }

//                Constants.TABLE_MANAGEMENT -> {
//                    sub_title_txt.text = "Table Configurator"
//                    navController.navigate(R.id.TableConfigurationFragment)
//                }

            }
            Handler().postDelayed({
                viewModel.drawerAppModuleItemSelection(mainContainerPosition, position)
            }, 500)

        }

    }

    override fun onItemClick(dashboardDrawerDTO: DashboardDrawerDTO) {
        if (!dashboardDrawerDTO.name.isBlank()) {
            if (dashboardDrawerDTO.name == Constants.LOG_OUT) {
                viewModel.showLogoutDialog()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

    }

    override fun updateProfile() {
        binding.navigationMenu.profileImage.setImageURI(Constants.PROFILE_IMAGE_URI) // Drawer Profile Image
        binding.content.profileImage.setImageURI(Constants.PROFILE_IMAGE_URI) // Dashboard Toolbar Profile Image
    }


    private var isBackPressed = false

    override fun onBackPressed() {

        if (navController.currentDestination?.id == R.id.DashboardFragment) {

            Handler().postDelayed({
                isBackPressed = false
            }, 2000)

            if (isBackPressed) {
//                super.onBackPressed()
                finish()
            } else {
                isBackPressed = true
                Toast.makeText(this, "Press back again to Exit", Toast.LENGTH_SHORT).show()
            }

        } else {
            navController.navigateUp()
        }

    }

}