//luca.olivieri@univr.it
//https://tezos.gitlab.io/active/michelson.html#full-grammar

parser grammar MichelsonParser;

@ header
{package it.unive.michelsonlisa.antlr;}

options { tokenVocab = MichelsonLexer; }

data :
    block_elt
  | block
  | instruction 
  | int_const
  | STRING_DATA
  | BYTE_SEQUENCE_CONST
  | UNIT_DATA
  | TRUE_DATA
  | FALSE_DATA
  | pair_data
  | left_data
  | right_data
  | some_data
  | or_data
  | NONE_DATA;
 
pair_data : LROUNDBRACE PAIR_DATA data data data* RROUNDBRACE | LBRACE data SEMICOL data SEMICOL? (data SEMICOL?)* RBRACE;
left_data : LEFT_DATA data | LROUNDBRACE LEFT_DATA data RROUNDBRACE;
right_data : RIGHT_DATA data | LROUNDBRACE RIGHT_DATA data RROUNDBRACE;
some_data : SOME_DATA data | LROUNDBRACE SOME_DATA data RROUNDBRACE;
or_data : LROUNDBRACE data data RROUNDBRACE;

int_const : NAL_CONST | MINUS NAL_CONST;

instruction :  
    block
  | drop_instr
  | SWAP
  | dig_instr
  | dug_instr
  | if_none_instr 
  | if_left_instr
  | if_cons_instr
  | iter_instr
  | if_instr
  | loop_instr
  | loop_left_instr
  | dip_instr
  | FAILWITH
  | dup_instr
  | push_instr
  | some_instr
  | none_instr
  | unit_instr
  | never_instr
  | nil_instr
  | empty_set_instr
  | empty_map_instr
  | empty_big_map_instr
  | map_instr
  | get_instr
  | update_instr
  | lambda_instr
  | unpack_instr
  | contract_instr
  | sapling_empty_state
  | swap_instr
  | cons_instr
  | size_instr
  | mem_instr
  | exec_instr
  | apply_instr
  | failwith_instr
  | cast_instr
  | rename_instr
  | concat_instr
  | slice_instr
  | pack_instr
  | add_instr
  | sub_instr
  | mul_instr
  | ediv_instr
  | abs_instr
  | isnat_instr
  | int_instr
  | neg_instr
  | lsl_instr
  | lsr_instr
  | or_instr
  | and_instr
  | xor_instr
  | not_instr
  | compare_instr
  | eq_instr
  | neq_instr
  | lt_instr
  | gt_instr
  | le_instr
  | ge_instr
  | self_instr
  | self_address_instr
  | transfer_tokens_instr
  | set_delegate_instr
  | implicit_account_instr
  | voting_power_instr
  | now_instr
  | level_instr
  | amount_instr
  | balance_instr
  | check_signature_instr
  | blake2b_instr
  | keccak_instr
  | sha3_instr
  | sha256_instr
  | sha512_instr
  | hash_key_instr
  | source_instr
  | sender_instr
  | address_instr
  | chain_id_instr
  | total_voting_power_instr
  | pairing_check_instr
  | sapling_verify_update_instr
  | ticket_instr
  | read_ticket_instr
  | split_ticket_instr
  | join_tickets_instr
  | open_chest_instr
  | pair_instr
  | left_instr
  | right_instr
  | car_instr
  | cdr_instr
  | create_contract_instr
  | unpair_instr
  | macro
  ;

drop_instr : DROP VAR_ANNOTATION* NAL_CONST?;
dup_instr : DUP VAR_ANNOTATION* NAL_CONST? ;
dig_instr : DIG NAL_CONST?;
dug_instr : DUG NAL_CONST?;
push_instr : PUSH VAR_ANNOTATION* type data ;
some_instr : SOME VAR_ANNOTATION*;
none_instr : NONE VAR_ANNOTATION* type ;
unit_instr : UNIT VAR_ANNOTATION*;
never_instr : NEVER VAR_ANNOTATION*;
if_none_instr : IF_NONE block block;
car_instr : CAR (SPECIAL_FIELD_ANNOTATION | FIELD_ANNOTATION |  VAR_ANNOTATION)*;
cdr_instr : CDR (SPECIAL_FIELD_ANNOTATION | FIELD_ANNOTATION |  VAR_ANNOTATION)*;
pair_instr : PAIR (SPECIAL_FIELD_ANNOTATION | FIELD_ANNOTATION | VAR_ANNOTATION)* NAL_CONST?;
unpair_instr : UNPAIR (SPECIAL_VAR_ANNOTATION* | FIELD_ANNOTATION |  VAR_ANNOTATION*) (SPECIAL_VAR_ANNOTATION* | FIELD_ANNOTATION |  VAR_ANNOTATION*) NAL_CONST?;
left_instr : LEFT (SPECIAL_FIELD_ANNOTATION | FIELD_ANNOTATION |  VAR_ANNOTATION)* type;
right_instr : RIGHT (SPECIAL_FIELD_ANNOTATION | FIELD_ANNOTATION |  VAR_ANNOTATION)* type;
if_left_instr : IF_LEFT block block;
nil_instr : NIL VAR_ANNOTATION* type;
if_cons_instr : IF_CONS block block;
empty_set_instr : EMPTY_SET VAR_ANNOTATION* comparable_type;
empty_map_instr : EMPTY_MAP VAR_ANNOTATION* comparable_type type;
empty_big_map_instr : EMPTY_BIG_MAP VAR_ANNOTATION* comparable_type type;
map_instr : MAP VAR_ANNOTATION* block;
iter_instr : ITER block;
get_instr : GET VAR_ANNOTATION* NAL_CONST?;
update_instr : UPDATE VAR_ANNOTATION* NAL_CONST?;
if_instr : IF block block;
loop_instr : LOOP block;
loop_left_instr :  LOOP_LEFT block;
lambda_instr : LAMBDA VAR_ANNOTATION* type type block;
dip_instr : DIP NAL_CONST? block;
unpack_instr : UNPACK VAR_ANNOTATION* type;
contract_instr : CONTRACT (FIELD_ANNOTATION | WVAR_ANNOTATION)? type;
create_contract_instr : CREATE_CONTRACT VAR_ANNOTATION* VAR_ANNOTATION* LBRACE contract RBRACE;
sapling_empty_state : SAPLING_EMPTY_STATE VAR_ANNOTATION* NAL_CONST;
swap_instr : SWAP VAR_ANNOTATION*;
cons_instr : CONS VAR_ANNOTATION*;
size_instr : SIZE VAR_ANNOTATION*;
mem_instr : MEM VAR_ANNOTATION*;
exec_instr : EXEC VAR_ANNOTATION*;
apply_instr : APPLY VAR_ANNOTATION*;
failwith_instr : FAILWITH VAR_ANNOTATION*;
cast_instr : CAST type VAR_ANNOTATION*;
rename_instr : RENAME VAR_ANNOTATION*;
concat_instr : CONCAT VAR_ANNOTATION*;
slice_instr : SLICE VAR_ANNOTATION*;
pack_instr : PACK VAR_ANNOTATION*;
add_instr : ADD VAR_ANNOTATION*;
sub_instr : SUB VAR_ANNOTATION*;
mul_instr : MUL VAR_ANNOTATION*;
ediv_instr : EDIV VAR_ANNOTATION*;
abs_instr : ABS VAR_ANNOTATION*;
isnat_instr : ISNAT VAR_ANNOTATION*;
int_instr : INT VAR_ANNOTATION*;
neg_instr : NEG VAR_ANNOTATION*;
lsl_instr : LSL VAR_ANNOTATION*;
lsr_instr : LSR VAR_ANNOTATION*;
or_instr : OR VAR_ANNOTATION*;
and_instr : AND VAR_ANNOTATION*;
xor_instr : XOR VAR_ANNOTATION*;
not_instr : NOT VAR_ANNOTATION*;
compare_instr : COMPARE VAR_ANNOTATION*;
eq_instr : EQ VAR_ANNOTATION*;
neq_instr : NEQ VAR_ANNOTATION*;
lt_instr : LT VAR_ANNOTATION*;
gt_instr : GT VAR_ANNOTATION*;
le_instr : LE VAR_ANNOTATION*;
ge_instr : GE VAR_ANNOTATION*;
self_instr : SELF (FIELD_ANNOTATION | VAR_ANNOTATION)*;
self_address_instr : SELF_ADDRESS (FIELD_ANNOTATION | VAR_ANNOTATION)*;
transfer_tokens_instr : TRANSFER_TOKENS VAR_ANNOTATION*;
set_delegate_instr : SET_DELEGATE VAR_ANNOTATION*;
implicit_account_instr : IMPLICIT_ACCOUNT VAR_ANNOTATION*;
voting_power_instr : VOTING_POWER VAR_ANNOTATION*;
now_instr : NOW VAR_ANNOTATION*;
level_instr : LEVEL VAR_ANNOTATION*;
amount_instr : AMOUNT VAR_ANNOTATION*;
balance_instr : BALANCE VAR_ANNOTATION*;
check_signature_instr : CHECK_SIGNATURE VAR_ANNOTATION*;
blake2b_instr : BLAKE2B VAR_ANNOTATION*;
keccak_instr : KECCAK VAR_ANNOTATION*;
sha3_instr : SHA3 VAR_ANNOTATION*;
sha256_instr : SHA256 VAR_ANNOTATION*;
sha512_instr : SHA512 VAR_ANNOTATION*;
hash_key_instr : HASH_KEY VAR_ANNOTATION*;
source_instr : SOURCE VAR_ANNOTATION*;
sender_instr : SENDER VAR_ANNOTATION*;
address_instr : ADDRESS VAR_ANNOTATION*;
chain_id_instr : CHAIN_ID VAR_ANNOTATION*;
total_voting_power_instr : TOTAL_VOTING_POWER VAR_ANNOTATION*;
pairing_check_instr : PAIRING_CHECK VAR_ANNOTATION*;
sapling_verify_update_instr : SAPLING_VERIFY_UPDATE VAR_ANNOTATION*;
ticket_instr : TICKET VAR_ANNOTATION*;
read_ticket_instr : READ_TICKET VAR_ANNOTATION*;
split_ticket_instr : SPLIT_TICKET VAR_ANNOTATION*;
join_tickets_instr : JOIN_TICKETS VAR_ANNOTATION*;
open_chest_instr : OPEN_CHEST VAR_ANNOTATION*;


type :  comparable_type 
  | option_type
  | list_type
  | set_type
  | contract_type
  | ticket_type
  | pair_type
  | or_type
  | lambda_type
  | map_type
  | big_map_type
  | sapling_transaction_type
  | sapling_state_type
  | BLS12_381_G1_TYPE
  | BLS12_381_G2_TYPE
  | BLS12_381_FR_TYPE
  | OPERATION_TYPE
  | CHEST_TYPE
  | CHEST_KEY_TYPE
  | ( LROUNDBRACE ( BLS12_381_G1_TYPE
  | BLS12_381_G2_TYPE
  | BLS12_381_FR_TYPE
  | OPERATION_TYPE
  | CHEST_TYPE
  | CHEST_KEY_TYPE) (TYPE_ANNOTATION | FIELD_ANNOTATION)* RROUNDBRACE)
  ;

option_type : OPTION_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type | LROUNDBRACE OPTION_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type RROUNDBRACE;
list_type : LIST_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type | LROUNDBRACE LIST_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type RROUNDBRACE;
set_type : SET_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type | LROUNDBRACE SET_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type RROUNDBRACE;
contract_type : CONTRACT_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type | LROUNDBRACE CONTRACT_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type RROUNDBRACE;
ticket_type : TICKET_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type | LROUNDBRACE TICKET_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type RROUNDBRACE;
pair_type : LROUNDBRACE  PAIR_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type type type* RROUNDBRACE ;
or_type : OR_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type type | LROUNDBRACE OR_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type type RROUNDBRACE;
lambda_type : LAMBDA_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type type | LROUNDBRACE LAMBDA_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* type type RROUNDBRACE;
map_type : MAP_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type type | LROUNDBRACE MAP_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type type RROUNDBRACE;
big_map_type : BIG_MAP_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type type | LROUNDBRACE BIG_MAP_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type type RROUNDBRACE;
sapling_transaction_type : SAPLING_TRANSACTION_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* NAL_CONST | LROUNDBRACE SAPLING_TRANSACTION_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* NAL_CONST RROUNDBRACE;
sapling_state_type : SAPLING_STATE_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* NAL_CONST | LROUNDBRACE SAPLING_STATE_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* NAL_CONST RROUNDBRACE;

comparable_type : 
   UNIT_TYPE
  | NEVER_TYPE
  | BOOL_TYPE
  | INT_TYPE
  | NAT_TYPE
  | STRING_TYPE
  | CHAIN_ID_TYPE
  | BYTES_TYPE
  | MUTEZ_TYPE
  | KEY_HASH_TYPE
  | KEY_TYPE
  | SIGNATURE_TYPE
  | TIMESTAMP_TYPE
  | ADDRESS_TYPE
  | ( LROUNDBRACE ( UNIT_TYPE
  | NEVER_TYPE
  | BOOL_TYPE
  | INT_TYPE
  | NAT_TYPE
  | STRING_TYPE
  | CHAIN_ID_TYPE
  | BYTES_TYPE
  | MUTEZ_TYPE
  | KEY_HASH_TYPE
  | KEY_TYPE
  | SIGNATURE_TYPE
  | TIMESTAMP_TYPE
  | ADDRESS_TYPE) (TYPE_ANNOTATION | FIELD_ANNOTATION)* RROUNDBRACE)
  | option_comptype
  | or_comptype
  | pair_comptype
  ;
  
option_comptype : OPTION_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type | LROUNDBRACE OPTION_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type RROUNDBRACE;
or_comptype : OR_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type comparable_type | LROUNDBRACE OR_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type comparable_type RROUNDBRACE;
pair_comptype : PAIR_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type comparable_type comparable_type * | LROUNDBRACE PAIR_TYPE (TYPE_ANNOTATION | FIELD_ANNOTATION)* comparable_type FIELD_ANNOTATION* comparable_type comparable_type * RROUNDBRACE;

code : CODE block SEMICOL?;

parameter : PARAMETER FIELD_ANNOTATION? type SEMICOL;

storage : STORAGE FIELD_ANNOTATION? type SEMICOL;

contract : LBRACE (parameter? storage? code) RBRACE | LBRACE (storage?  parameter? code) RBRACE | (parameter? storage? code) | (storage?  parameter? code);

block : LBRACE (data SEMICOL?)* RBRACE ;
block_elt : LBRACE ELT_DATA ( data data SEMICOL*)+ (ELT_DATA ( data data SEMICOL*)+)* RBRACE ;
 
macro : 
  CMPEQ
 | CMPNEQ
 | CMPLT
 | CMPGT
 | CMPLE
 | CMPGE
 | IFEQ
 | IFNEQ
 | IFLT
 | IFGT
 | IFLE
 | IFGE
 | IFCMPEQ
 | IFCMPNEQ
 | IFCMPLT
 | IFCMPGT
 | IFCMPLE
 | IFCMPGE
 | ASSERT
 | ASSERT_EQ
 | ASSERT_NEQ
 | ASSERT_LT
 | ASSERT_LE
 | ASSERT_GT
 | ASSERT_GE
 | ASSERT_CMPEQ
 | ASSERT_CMPNEQ
 | ASSERT_CMPLT
 | ASSERT_CMPGT
 | ASSERT_CMPLE
 | ASSERT_CMPGE
 | ASSERT_NONE
 | ASSERT_SOME
 | ASSERT_LEFT
 | ASSERT_RIGHT
 | FAIL;