package edu.ort.pastillapp.listener

import edu.ort.pastillapp.models.Medicine

interface OnImageViewClickListener {
    fun onImageViewClick(medication: Medicine)
}