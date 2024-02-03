This is a snake game with its logic entirely segregated on server side.

Game is streamed using RSocket's request-stream protocol.

Input is fired in from the javascript client side.

New game is initiated by refreshing the page.

No score is maintained yet.

I don't think application is thread safe. I have not tested it yet.

I'm using `npx webpack` command to bundle the javascript files (It contains some node modules). 

This is the initial working version of the game. I will be adding more features to it.


To run the application:
1. Clone the repository
2. Run application with gradle or Intellij.
3. Run `npx webpack` in the terminal to bundle the javascript files. (In root directory of the project)
4. Open ./dist/index.html in the browser.
