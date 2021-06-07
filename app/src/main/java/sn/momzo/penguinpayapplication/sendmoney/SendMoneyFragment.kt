package sn.momzo.penguinpayapplication.sendmoney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.getViewModel
import sn.momzo.penguinpayapplication.*
import sn.momzo.penguinpayapplication.data.Country
import sn.momzo.penguinpayapplication.databinding.FragmentSendMoneyBinding
import sn.momzo.penguinpayapplication.utils.PhoneTextFormatter
import sn.momzo.penguinpayapplication.utils.TextDrawable
import java.nio.file.Files.delete


/**
 * A simple [Fragment] subclass.
 * Use the [SendMoneyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendMoneyFragment : Fragment() {
    private var _binding: FragmentSendMoneyBinding? = null
    private val binding get() = _binding!!
    private lateinit var sendMoneyViewModel: SendMoneyViewModel
    private var selectedCountry: Country? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendMoneyViewModel = getViewModel()
        manageInputs()
        getRates()
        setupSpinner()
        handleClicks()
    }

    private fun manageInputs() {
        binding.apply {
            firstName.removeErrorWhenTextChanged()
            lastName.removeErrorWhenTextChanged()
            recipientAmount.removeErrorWhenTextChanged()
            recipientPhoneNumber.removeErrorWhenTextChanged()
        }
        binding.amountSender.editText?.addTextChangedListener { amountSender->
            if (amountSender.isNullOrBlank()) {
                binding.recipientAmount.editText?.setText(null)
            } else {
                sendMoneyViewModel.calculateRecipientAmount(
                    amountSender.toString(),
                    selectedCountry?.currencySymbol.orEmpty()).observe(viewLifecycleOwner, { amount ->
                        amount?.let {
                            binding.recipientAmount.editText?.setText(it)
                        }
                })
            }
        }
    }

    private fun setupSpinner() {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Constants.countries.map { it.name }
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.countries.adapter = arrayAdapter
        }

        binding.countries.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCountry =  Constants.countries[position]
                setDialCode()
                getSelectedRate()

                binding.recipientPhoneNumber.editText?.addTextChangedListener(
                    PhoneTextFormatter(
                        binding.recipientPhoneNumber.editText!!,
                        selectedCountry?.phoneNumberFormat.orEmpty()))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun getSelectedRate() {
        binding.rate.text = getString(R.string.selected_rate,
            sendMoneyViewModel.convertRateToBinary(selectedCountry?.currencySymbol.orEmpty()),
            selectedCountry?.currencySymbol.orEmpty())
    }

    private fun getRates() {
        sendMoneyViewModel.getRates().observe(viewLifecycleOwner, {
            it?.let {
                Logger.d("Nothing to do here")
                getSelectedRate()
            } ?: kotlin.run {
                Toast.makeText(requireContext(), getString(R.string.error_getting_rates), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun handleClicks() {
        binding.sendMoney.setOnClickListener {
            if (areAllInputsValid()) {
                showSuccess()
            }
        }
    }

    private fun showSuccess() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.apply {
            setCancelable(false)
            setMessage(resources.getString(R.string.transaction_processing))
            setPositiveButton(R.string.close) { dialogInterface, i ->
                reInitInputs()
                dialogInterface.dismiss()
            }
            show()
        }
    }

    private fun reInitInputs() {
        binding.apply {
            firstName.editText?.setText(null)
            lastName.editText?.setText(null)
            recipientPhoneNumber.editText?.setText(null)
            amountSender.editText?.setText(null)
            recipientAmount.editText?.setText(null)
        }

    }

    private fun setDialCode() {
        val phoneCode = TextDrawable(context)
        phoneCode.apply {
            text = String.format("(+%s)", selectedCountry?.phoneNumberPrefix)
            textSize = 16f
            setTextColor(resources.getColor(R.color.black))
        }
        binding.recipientPhoneNumber.editText?.apply {
            compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.dimen_8)
            setCompoundDrawablesWithIntrinsicBounds(phoneCode, null, null, null)
        }
    }



    private fun areAllInputsValid(): Boolean {
        binding.apply {
            if (firstName.editText?.text.isNullOrBlank()) {
                firstName.showError()
                return false
            }

            if (lastName.editText?.text.isNullOrBlank()) {
                lastName.showError()
                return false
            }

            if (recipientPhoneNumber.editText?.text.isNullOrBlank()) {
                recipientPhoneNumber.showError()
                return false
            }

            if (amountSender.editText?.text.isNullOrBlank()) {
                amountSender.showError()
                return false
            }

        }
        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}