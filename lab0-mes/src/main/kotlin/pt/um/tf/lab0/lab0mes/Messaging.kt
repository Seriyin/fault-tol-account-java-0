package pt.um.tf.lab0.lab0mes

data class Message(val op : Int, val mov : Int)

data class Reply(val op : Int, val res : Boolean, val balance : Int)