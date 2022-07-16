package chain
import org.json.JSONObject

class Block(val index: Int, var data: List<Transactions>, var prevHash: String = "") {
    var idx = index
    var ts = timestamp()
    var ph = prevHash
    var hash = calculateHash()
    var nonce = 0


    fun calculateHash(): String {
        //JSON.stringify(data)
        val json = JSONObject()
        json.put("data", data)
        return sha256(idx.toString() + ts + json.toString() + ph + nonce).toString()
    }
    fun mineBlock(difficulty: Int) {
        while (hash.substring(0, difficulty) != "0".repeat(difficulty)) {
            nonce++
            hash = calculateHash()
        }
    }

    fun includesValidTransactions(): Boolean {
        for(i in data){
            if(!i.isValid()){
                return false
            }
        }
        return true
    }
}