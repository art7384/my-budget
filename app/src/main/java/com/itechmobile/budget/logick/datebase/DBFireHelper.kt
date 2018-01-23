package com.itechmobile.budget.logick.datebase

//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
import com.itechmobile.budget.model.TracsationModel
import com.itechmobile.budget.model.UserModel


/**
 * Created by artem on 07.09.17.
 */
class DBFireHelper {

//    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference

    /*

    String
    Long
    Double
    Boolean

    Map<String, Object>
    List<Object>

     */

    var result: HashMap<String, Any> = HashMap()

    fun saveUser(user: UserModel){
//        mDatabase.child("users").child(user.id).child("username").setValue(user.name)
    }

    fun saveTracsation(tracsation: TracsationModel){
        //mDatabase.child("tracsation").child(user.id)
    }


}