package com.example.property.ui.document

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.property.data.models.Document

class DocViewModel: ViewModel() {
    var doc = Document()

    private var docUri: Uri? = null

    private val repository = DocRepository.getInstance()

    //response
    var getDocResult: MutableLiveData<ArrayList<Document>> = MutableLiveData()
    var addDocResult: MutableLiveData<String> = MutableLiveData()

    fun getAllDocs(){
        repository.getAllDocs(getDocResult)
    }

    fun onAddDocClicked(view: View){
        repository.addDoc(doc, docUri, addDocResult)
    }

    fun setDocUri(uri: Uri?){
        docUri = uri
    }

    fun getAddDocErrorMsg(): String {
        return repository.getAddDocErrorMsg()
    }

    fun getGetDocErrorMsg(): String {
        return repository.getGetDocErrorMsg()
    }
}