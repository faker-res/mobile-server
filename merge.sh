#!/bin/bash
git checkout prod
git pull

git checkout prerelease
git pull
git merge prod
git push

git checkout test
git pull
git merge prerelease
git push

git checkout develop
git pull
git merge test
git push


