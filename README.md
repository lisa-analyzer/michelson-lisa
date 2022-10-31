# MichelsonLiSA
MichelsonLiSA: A Static Analyzer for Smart Contracts written in [Michelson language](https://tezos.gitlab.io/michelson-reference/)  (Tezos Blockchain)

## How to use MichelsonLiSA
The main class is [MichelsonLiSA](michelson-lisa/src/main/java/it/unive/michelsonlisa/MichelsonLiSA.java) and it expects at least three paramaters:
- `-i path`: the Michelson file to be analyzed
- `-o path`: the output directory
- `-a analysis`: the analysis to perform, where `analysis` is one of the following values:
	-  `sign` sign analysis
	- `ucci` untrusted cross-contract analysis
- `-d` dump additional the analysis informations (optional)

### Example of command line

`-i mycontract.tz -o myoutputdir -a ucci -d`
