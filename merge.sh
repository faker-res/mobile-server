#!/bin/bash
git checkout prod02
git pull

git checkout local
git pull
git merge prod02
git push

git checkout develop
git pull
git merge local 
git push


