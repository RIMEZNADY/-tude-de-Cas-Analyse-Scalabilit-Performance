@echo off
cd /d "%~dp0"
git init
git remote remove origin 2>nul
git remote add origin https://github.com/RIMEZNADY/-tude-de-Cas-Analyse-Scalabilit-Performance.git
git add .
git commit -m "push"
git branch -M main
git push -u origin main --force

