package com.example.property.ui.document

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.property.app.Config
import com.example.property.data.models.Document
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class DocRepository {

    private val storage= FirebaseStorage.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference(Config.DOCUMENT_TABLE)
    private var getDocErrorMsg: String? = null
    private var addDocErrorMsg: String? = null

    companion object{
        private var instance: DocRepository? = null

        fun getInstance(): DocRepository{
            if(instance == null)
                instance = DocRepository()
            return instance!!
        }
    }

    fun getAllDocs(result: MutableLiveData<ArrayList<Document>>) {
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val mList = ArrayList<Document>()
                for(data in snapshot.children){
                    val doc = data.getValue(Document::class.java)
                    if(doc != null)
                        mList.add(doc)
                }
                result.value = mList
            }

            override fun onCancelled(error: DatabaseError) {
                getDocErrorMsg = error.message
                result.value = null
            }
        })
    }

    fun getGetDocErrorMsg(): String{
        return getDocErrorMsg!!
    }

    fun addDoc(doc: Document, uri: Uri?, result: MutableLiveData<String>){
        if(uri != null){
            val location = uri.lastPathSegment + ".jpg"
            val ref = storage.getReference(location)
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener {
                            doc.url = it.toString()
                            addDownloadUrlToDatabase(doc)
                            result.value = "Document has been uploaded"
                        }
                        .addOnFailureListener{
                            addDocErrorMsg = it.message.toString()
                            result.value = null
                        }
                }
                .addOnFailureListener{
                    addDocErrorMsg = it.message.toString()
                    result.value = null
                }
        }
    }

    private fun addDownloadUrlToDatabase(doc: Document) {
        val key = dbRef.push().key
        dbRef.child(key!!).setValue(doc)
    }

    fun getAddDocErrorMsg(): String{
        return addDocErrorMsg!!
    }
}