import chain.Blockchain
import chain.Transactions
import chain.Wallet

//simple prove of work blockchain.

fun main() {
    val blockchain = Blockchain()
    val FabriceWallet = Wallet(blockchain)
    val cecileWallet = Wallet(blockchain)
    val marcWallet = Wallet(blockchain)

    //+10 coins to Fabrice's wallet
    blockchain.minePendingTransactions(FabriceWallet.pubKey)
    //0.5 coins to Cecile's wallet
    blockchain.minePendingTransactions(cecileWallet.pubKey)
    val tx1 = Transactions(FabriceWallet.pubKey, cecileWallet.pubKey, 6.0)
    blockchain.sign(tx1, FabriceWallet,FabriceWallet.privKey)
    blockchain.addTransaction(tx1)
    blockchain.minePendingTransactions(cecileWallet.pubKey)
    println("Balance of Marc: ${blockchain.getBalanceOfAddress(marcWallet.pubKey)}")
    println("Balance of Fabrice: ${blockchain.getBalanceOfAddress(FabriceWallet.pubKey)}")
    println("Balance of Cecile: ${blockchain.getBalanceOfAddress(cecileWallet.pubKey)}")



    println(blockchain.jsonStringify())
    println("Is chain valid? ${blockchain.isChainValid()}")
}
//28a34e45561ffc1c2b9be2c3b0288c0ff98ac1d5be20181fdaf206e2e9f0a24d
//28a34e45561ffc1c2b9be2c3b0288c0ff98ac1d5be20181fdaf206e2e9f0a24d

