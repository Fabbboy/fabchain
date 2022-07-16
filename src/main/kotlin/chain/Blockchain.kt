package chain
import org.json.JSONArray
import org.json.JSONObject

class Blockchain {
    val chain = mutableListOf<Block>()
    private val difficulty = 2
    private var pendingTransactions = mutableListOf<Transactions>()
    private var miningReward : Double = 0.5
    var wallets = mutableListOf<Wallet>()

    fun addWallet(wallet: Wallet) {
        wallets.add(wallet)
    }
    fun removeWallet(wallet: Wallet) {
        wallets.remove(wallet)
    }
    fun getWallet(Publicaddress: String): Wallet? {
        for (wallet in wallets) {
            if (wallet.pubKey == Publicaddress) {
                return wallet
            }
        }
        return null
    }


    init{
        chain.add(createGenesisBlock())
    }
    fun createGenesisBlock(): Block {
        miningReward += 9.5
        return Block(0, emptyList(), "0")
    }
    fun getLatestBlock() : Block {
        return chain.last()
    }
    fun getNextIdx() : Int {
        return chain.size
    }
    fun minePendingTransactions(miningRewardAddress: String) {
        if(!isChainValid()) {
            println("Invalid chain")
            return
        }
        val penBlock = Block(getNextIdx(), pendingTransactions, getLatestBlock().hash)
        penBlock.mineBlock(difficulty)
        chain.add(penBlock)
        //reset pending transactions
        pendingTransactions = mutableListOf<Transactions>()
        //add a new trans
        val rewardTransactions = Transactions(null.toString(),miningRewardAddress, miningReward.toDouble())
        rewardTransactions.signTransactionsFromRoot()
        pendingTransactions.add(rewardTransactions)
        miningReward = 0.5
    }
    fun addTransaction(transactions: Transactions){

        if(transactions.fromAddress.isEmpty() || transactions.toAddress.isEmpty()) {
            println("Transaction must include from and to address")
            return
        }
        if(!transactions.isValid()){
            println("Transaction is not valid")
            return
        }

        pendingTransactions.add(transactions)
    }
    fun getBalanceOfAddress(address: String) : Double {
        var balance : Double = 0.0
        for(block in chain) {
            for(transaction in block.data) {
                if(transaction.fromAddress == address) {
                    balance -= transaction.amount
                }
                if(transaction.toAddress == address) {
                    balance += (transaction.amount)
                }
            }
        }
        return balance
    }
    fun isChainValid() : Boolean {
        for (i in 1 until chain.size) {
            val currentBlock = chain[i]
            val previousBlock = chain[i-1]
            if (currentBlock.hash != currentBlock.calculateHash()) {
                return false
            }
            if(!currentBlock.includesValidTransactions()){
                return false
            }
            if (currentBlock.prevHash != previousBlock.hash) {
                return false
            }
        }
        return true
    }
    fun jsonStringify(): String {
        val json = JSONArray()
        for (block in chain) {
            //index: idx
            //data: data
            //prevHash: prevHash
            //hash: hash
            val jsonBlock = JSONObject()
            jsonBlock.put("index", block.index)
            jsonBlock.put("timestamp", block.ts)
            jsonBlock.put("data", block.data)
            jsonBlock.put("nonce", block.nonce)
            jsonBlock.put("prevHash", block.prevHash)
            jsonBlock.put("hash", block.hash)
            json.put(jsonBlock)
        }
        return json.toString(4)
    }
    fun burnAmount(wallet: Wallet, amount: Int) {
        val newTrans = Transactions(null.toString(), wallet.pubKey, -amount.toDouble())
        pendingTransactions.add(newTrans)
    }
    fun calculateGasPrice(transactions: Transactions, minus: Boolean = false) : Double {
        //100 / (gasLimit * gasPrice) = gasToPay : 100 / (0.5 * 10) = 20

        val totalSpend = getTotalTransactions()
        if(minus){
            return -(totalSpend / (transactions.gasLimit * transactions.gasPrice))
        }
        return (totalSpend / (transactions.gasLimit * transactions.gasPrice))
    }
    fun getTotalTransactions() : Double{
        var ret : Double = 0.0
        for(i in pendingTransactions){
            ret += i.amount
        }
        return ret
    }
    fun sign(transactions: Transactions, wallet: Wallet, privKey : String){
        val sig = transactions.signTransaction(wallet, privKey)
        if(!sig){
            //cancel transaction
            pendingTransactions.remove(transactions)
            return
        }
        val isVa = transactions.isValid()
        if(!isVa){
            //remove complete block
            chain.remove(getLatestBlock())
            return
        }
        val gasFeeTrans = Transactions(null.toString(), wallet.pubKey, calculateGasPrice(transactions, true))
        gasFeeTrans.signTransactionsFromRoot()
        addTransaction(gasFeeTrans)
        miningReward += calculateGasPrice(transactions)
    }
}

//20  100