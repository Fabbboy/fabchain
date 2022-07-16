package chain

class Transactions(val fromAddress: String, val toAddress: String, var amount: Double) {
    var hashTx: Any = ""
    val gasPrice: Int = 10
    var gasLimit : Float = 0.5F

    private fun calculateHash() {
        hashTx = sha256(fromAddress + toAddress + amount + gasPrice + gasLimit)
    }
    fun signTransactionsFromRoot(){
        calculateHash()
    }
    fun signTransaction(wallet : Wallet, privKey : String): Boolean {
        if(wallet.pubKey != this.fromAddress){
            println("You cannot sign transactions for other wallets")
            return false
        }else if(this.fromAddress == this.toAddress){
            println("Cannot sign transaction to same address")
            println(toAddress)
            println(fromAddress)
            return false
        }else if(this.amount > wallet.getBalance() + wallet.blockchain.calculateGasPrice(this, false)){
            println("Insufficient funds: " + wallet.getBalance())
            return false
        }else if(wallet.privKey != privKey){
            println("You cannot sign transactions for other wallets")
            return false
        }
        calculateHash()
        return true
    }
    fun isValid(): Boolean {
        //check if sign exists
        if (hashTx == "") {
            println("Transaction is not signed")
            return false
        }
        return true
    }

}
//https://www.youtube.com/watch?v=kWQ84S13-hw
//https://www.youtube.com/watch?v=AQV0WNpE_3g