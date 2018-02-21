package pt.um.tf.lab0.lab0srv

class Account {
    var balance : Int = 0

    fun movement(mov : Int): Boolean {
        var res = true
        if (mov > 0) {
            balance += mov
        }
        else {
            if (-mov > balance) {
                res = false
            }
            else {
                balance -= -mov
            }
        }
        return res
    }
}