package com.example.sepo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.data.response.DoctorResponseItem
import com.example.sepo.databinding.ItemDoctorBinding

class DoctorAdapter(
    private val list: List<DoctorResponseItem>,
    private val onItemClick: (DoctorResponseItem) -> Unit

) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: DoctorResponseItem) {
            binding.tvName.text = doctor.name
            binding.tvSpecial.text = "Spesialis ${doctor.special}"
            binding.tvHospitalAddress.text = doctor.addressHospital
            binding.tvNumber.text = doctor.numberPhone
            binding.tvDayWork.text = doctor.time

            binding.root.setOnClickListener {
                onItemClick(doctor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}

