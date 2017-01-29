#!/bin/bash

echo Computer Networks and Distributed Systems Coursework - Install Utility
echo \(c\) Daniele Sgandurra, Imperial College London, Mar 2016

fromdos policy
for i in *.sh; do
    fromdos $i
done
chmod u+x *.sh

echo Done!
