set INTER;  # intersections

param entr in INTER;           # entrance to road network
param exit in INTER, <> entr;  # exit from road network

set ROADS within (INTER) cross (INTER );

param BIG_NUMBER = 9999999999;
param flow {ROADS} >= 0;        
var Use {(i,j) in ROADS}  binary, := 1; 

maximize Total_flow: max {(i,j) in ROADS}
		 ((1-Use[i,j]) * BIG_NUMBER + flow[i,j] * Use[i,j]);

s.t. Start:  sum {(entr,j) in ROADS} Use[entr,j] = 1;

s.t. Balance {k in INTER diff {entr,exit}}:
   sum {(i,k) in ROADS} Use[i,k] = sum {(k,j) in ROADS} Use[k,j];
   
s.t. NoRepeat {k in INTER diff {entr,exit}}:
   sum {(i,k) in ROADS} Use[i,k] <= 1;