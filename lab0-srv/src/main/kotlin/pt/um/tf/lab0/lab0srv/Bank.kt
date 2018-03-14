package pt.um.tf.lab0.lab0srv

interface Bank {
    fun movement(mov : Int) : Boolean
    fun balance() : Int
}