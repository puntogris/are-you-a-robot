import * as functions from 'firebase-functions'
import * as admin from 'firebase-admin'
const express = require('express');
const exphbs  = require('express-handlebars');
const helpers = require('handlebars-helpers')('math')

const app = express();

app.engine('hbs', exphbs({
  extname: 'hbs',
  defaultLayout:false,
  helpers: helpers
}));
app.set('view engine', 'hbs');

admin.initializeApp()

exports.startMatchmaking = functions.https.onCall(async (data, context) => {
  const playerName = data.player
  const timestamp = admin.firestore.Timestamp.now()
  const playerOne = {
    name: "",
    score: 0
  }
  const playerTwo = {
    name: "loading..",
    score: 0
  }
  //check if a match exist
   try {
    const querysnap = await admin.firestore().collection("matches").where("full", "==", false).get()
    if (querysnap.docs.length !== 0) {
      //add player to existing match
      const matches = querysnap.docs
      const docId = matches[0].id
      playerTwo.name = playerName
      await admin.firestore().doc(`matches/${docId}`).update({
        playerTwo: playerTwo,
        full: true
      })      
      return docId
    }
    else {
      //creates a new match
      playerOne.name = playerName
      const match = {
        timestamp: timestamp,
        full: false,
        playerOne: playerOne,
        playerTwo: playerTwo
      }
      const docRef = admin.firestore().collection("matches").doc()
      await admin.firestore().doc(`matches/${docRef.id}`).set(match)
      return docRef.id
    }
  }
  catch (error) {
    return error
  } 
})

exports.unsubscribeToMatch = functions.https.onCall(async (data, context) => { 
  try {
    const promises:any = []
    const querysnap = await admin.firestore().collection("matches").where('playerOne.name', "==", data.player).get()
    querysnap.forEach(function (doc) {
      if (doc.get("full") === false) {
        const p = doc.ref.delete()
        promises.push(p)
      }
    })
    return Promise.all(promises)
  }
  catch (error) {
    return error
  }
})

//Website
app.get('/',async(request:any,response:any)=>{
  try {
    const rankings:any = []
    //response.set('Cache-Control','public, max-age = 300, s-maxage = 600')
    const querysnap = await getRankings()
    querysnap.forEach(doc =>{
      rankings.push(doc.data())
    })
    response.render('index',{rankings})  
  } 
  catch {
    response.send("Error getting the data")
  }
})

exports.app = functions.https.onRequest(app)

function getRankings(){
  const ref = admin.firestore().collection("ranking").orderBy("score","desc").limit(14)
  return ref.get().then(snap => snap.docs)
}

//Needs blaze plan to work
//TODO: convert to async
// exports.cleanOldMatches = functions.pubsub.schedule('every 1 minute').onRun((context) => {
//   const actualTimestamp = admin.firestore.Timestamp.now()
//   const time = new admin.firestore.Timestamp(actualTimestamp.seconds-3600,actualTimestamp.nanoseconds)
//   return admin.firestore().collection("matches").where("timestamp","<=",time).get()
//   .then(function(querysnap){
//     querysnap.forEach(function(doc){
//         doc.ref.delete()
//       .then(info=>{
//         return true
//       }).catch(error =>{
//         return error})
//     })
//   }).catch(error =>{
//     return error})  
// })