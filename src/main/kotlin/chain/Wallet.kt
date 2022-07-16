package chain
import chain.Transactions
import java.util.*


class Wallet(var blockchain: Blockchain) {
    var pubKey : String = ""
    var privKey : String = ""
    init{
        val lastNonce = blockchain.getLatestBlock().nonce
        //rand num
        var randNum = blockchain.getNextIdx() + lastNonce + timestamp() + blockchain.wallets.size + rand(1, 9999999999)
        //calculate pubKey based on the: timestamp, random number between 0 and the last nonce, and the block hight
        pubKey = sha256(timestamp() + randNum + blockchain.getNextIdx() - 1)
        //calculate privKey based on the: timestamp, random number between 0 and the last nonce, and the block hight mutlipliet by difficulty
        privKey = sha512((timestamp() + randNum + blockchain.getNextIdx() - 1) * blockchain.getNextIdx())
        blockchain.addWallet(this)
    }
    fun getBalance(): Double {
        return blockchain.getBalanceOfAddress(pubKey)
    }
    fun rand(start: Int, end: Long): Int {
        return Random(System.nanoTime()).nextInt((end - start + 1).toInt()) + start
    }
}