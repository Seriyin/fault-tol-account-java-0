package pt.um.tf.lab0.lab0mes

interface Bank {
    fun movement(mov : Int) : Boolean
    fun balance() : Int
}