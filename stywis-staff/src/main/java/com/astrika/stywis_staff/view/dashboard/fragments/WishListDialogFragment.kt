package com.astrika.stywis_staff.view.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.adapters.WishListAdapter
import com.astrika.stywis_staff.databinding.FragmentDialogWishlistBinding
import com.astrika.stywis_staff.models.WishListItemStatus
import com.astrika.stywis_staff.models.WishListItems
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.Utils
import com.astrika.stywis_staff.view.dashboard.viewmodels.WishListViewModel


class WishListDialogFragment(checkInUserId: Long?) : DialogFragment(),
    WishListAdapter.OnItemClickListener {

    lateinit var binding: FragmentDialogWishlistBinding
    lateinit var viewModel: WishListViewModel
    private lateinit var mContext: Context
    private lateinit var wishListAdapter: WishListAdapter
    private var progressBar = CustomProgressBar()
    var userId = checkInUserId
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate<FragmentDialogWishlistBinding>(
            inflater, R.layout.fragment_dialog_wishlist,
            container,
            false
        )


        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            WishListViewModel::class.java,
            this,
            binding.root
        )!!
        viewModel.checkInUserId = userId
//        viewModel.checkInUserId = 532L
//        mContext = container?.context!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initAdapter()
        observers()
        viewModel.populateWishList()

        return binding.root
    }

    private fun initAdapter() {
        wishListAdapter = WishListAdapter(requireContext(), this)
        binding.wishListRecyclerView.setHasFixedSize(true)
        binding.wishListRecyclerView.adapter = wishListAdapter

    }

    private fun observers() {
        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })

        viewModel.wishListMutableLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.items.isNullOrEmpty()) {
                wishListAdapter.arrayList = it.items ?: arrayListOf()
                viewModel.noWishListItemsFound.set(false)
            } else {
                viewModel.noWishListItemsFound.set(true)
            }
        })

        viewModel.onCloseDialogClick.observeChange(requireParentFragment(), Observer {
            dismiss()
        })
    }

    override fun onItemClick(position: Int, item: WishListItems, isRemove: Boolean) {

        item.itemId.let {
            if (it != null) {
                if (!isRemove) {
                    val status = if (item.status == WishListItemStatus.COMPLETED.name) {
                        WishListItemStatus.PENDING.name
                    } else {
                        WishListItemStatus.COMPLETED.name
                    }
                    viewModel.changeWishListItemStatus(it, status)
                } else {
                    viewModel.deleteWishListItem(it, false)
                }

            }


        }

    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }


}