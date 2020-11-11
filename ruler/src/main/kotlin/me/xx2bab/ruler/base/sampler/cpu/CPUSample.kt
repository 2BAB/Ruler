package me.xx2bab.ruler.base.sampler.cpu

data class CPUSample(
        val deviceHighDemandLevel: String, // Depends on frequency
        val processUtilization: String, // Depends on process idle & busy
        val cores: List<CPUInfo>
) {
    override fun toString(): String {
        return "CPUSample(deviceHighDemandLevel='$deviceHighDemandLevel'," +
                " processUtilization='$processUtilization'," +
                " cores=$cores)"
    }
}