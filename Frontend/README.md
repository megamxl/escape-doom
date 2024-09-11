# Escape Doom

## Quick Introduction

---
Escape Doom is a Web-App based on React, Vite and Redux.
It is meant to provide an interactive point-and-click
escape room.

## Installation and Setup instructions

---

Clone this repository. You will need node and npm installed on your machine.

Installation:

`npm install`

To start the Server:

`npm run dev`

## Folder Structure

---

### `Pages`

This folder should contain sub-folders for each individual page inside the application. Each sub-folder contains a
single root file that resembles the page (`main.tsx`) alongside with all files that only apply to this specific page
(includes `hooks` or `components`).

### `Components`

This folder is also broken down into sub-folders. These just help to categorize `components` further like "UI" for
buttons,
modals, cards etc. or "Form" for form specific controls...

### `Hooks`

The `hooks` folder contains every global `hook` in your entire project. Everything that might get repurposed in multiple
`components` and has a `hook` belongs in here :D

### `Assets`

The `assets` folder contains all images, css files, font files, etc. for your project. Pretty much anything that isn't
code related will be stored in this folder.

### `Context`

The `context` folder will be used to store Redux specific code that helps us track global states.

### `Data`

The `data` folder is similar to `assets` but will mostly be used for prop data like JSON files that mimic backend data.
This can als contain global constant variables (environment variables).

### `Utils`

This folder is for storing all utility functions such as formatters. All functions inside utils should be pure, if you
want to know what a pure function is, [read here](https://blog.webdevsimplified.com/2020-09/pure-functions)
