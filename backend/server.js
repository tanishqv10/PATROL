// server.js

require('dotenv').config();
const express = require('express');
const server = express();
const PORT = process.env.PORT || 3000;
const bcrypt = require('bcrypt');
const cors = require('cors');
const axios = require('axios');
const NEWS_API_KEY = process.env.NEWS_API_KEY;

server.use(express.json());
server.use(cors());

const { MongoClient, ServerApiVersion } = require('mongodb');
const uri = "mongodb+srv://tvarshne:44332211%40Tanishq@maincluster.c6exvxp.mongodb.net/?retryWrites=true&w=majority&appName=MainCluster";
const client = new MongoClient(uri, {
    serverApi: {
        version: ServerApiVersion.v1,
        strict: true,
        deprecationErrors: true,
    }
});

const nodemailer = require('nodemailer');

function genPassword(length) {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()';
    let result = '';
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'tanishqtemp10@gmail.com',
        pass: 'ikne vgsw rtrt pgww'
    }
});

client.connect()
    .then(() => {
        console.log("Connected successfully to MongoDB");

        const database = client.db("androidApp");
        const users = database.collection("userDetails");

        server.post('/addUser', async (req, res) => {

            // console.log("POST /addUser");
            // console.log(req.body);
            // return res.status(401).json({ message: 'not implemented'});
            const { email, password, fname, lname, dob, city, phone } = req.body;
            try {
                const existingUser = await users.findOne({ email: email });
                if (existingUser) {
                    return res.status(409).json({ message: 'A user with this email already exists.' });
                }
                const hashedPassword = await bcrypt.hash(password, 10);
                const result = await users.insertOne({ email: email, password: hashedPassword, fname: fname, lname: lname, dob: dob, city: city, phone: phone });
                res.status(201).json(result);
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error connecting to the database', error: error });
            }
        });

        server.get('/getUserDetails', async (req, res) => {
            const email = req.query.email;

            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    res.status(200).json({ fname: user.fname, lname: user.lname, dob: user.dob, city: user.city, phone: user.phone });
                } else {
                    res.status(404).json({ message: 'User not found.' });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error retrieving user from the database', error: error });
            }
        });

        server.get('/getHealthStatus', async (req, res) => {
            const email = req.query.email;

            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    res.status(200).json({ healthStatus: user.healthStatus });
                } else {
                    res.status(404).json({ message: 'User not found.' });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error retrieving user from the database', error: error });
            }
        });


        server.post('/updateProfile', async (req, res) => {
            const { email, city, phone } = req.body;

            // console.log("POST /updateProfile");

            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    const result = await users.updateOne(
                        { city: city },
                        { phone: phone }
                    );
                    if (result.modifiedCount === 1) {
                        res.status(200).json({ message: 'Profile updated!' });
                    } else {
                        res.status(400).json({ message: 'No changes made to the profile.' });
                    }
                } else {
                    res.status(404).json({ message: "User not found" });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error updating the password', error: error });
            }
        });

        server.post('/login', async (req, res) => {
            const { email, password } = req.body;
            // console.log("POST /login");
            // console.log(req.body);

            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    const isMatch = await bcrypt.compare(password, user.password);
                    if (isMatch) {
                        res.status(200).json({ message: "Login successful!", email: email });
                    } else {
                        res.status(401).json({ message: "Invalid credentials" });
                    }
                } else {
                    res.status(404).json({ message: "User not found" });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error connecting to the database', error: error });
            }
        });

        server.post('/favPlaces', async (req, res) => {
            const { email, favoritePlace } = req.body;

            console.log("--------------------------", req.body);
            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    const result = await users.updateOne(
                        { email: email },
                        { $push: { favPlaces: favoritePlace } }
                    );
                    if (result.modifiedCount === 1) {
                        res.status(200).json({ message: 'Favorite place added successfully!' });
                    } else {
                        res.status(400).json({ message: 'No changes made to the user.' });
                    }
                } else {
                    res.status(404).json({ message: "User not found" });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error updating the database', error: error });
            }
        });

        server.get('/getFavPlaces', async (req, res) => {
            const { email } = req.query;
            console.log("--------------------------------------------------", email, "-----------------------------------------------")
            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    console.log(user.favPlaces)
                    res.status(200).json({ favPlaces: user.favPlaces });
                } else {
                    res.status(404).json({ message: "User not found" });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error fetching from the database', error: error });
            }
        });


        server.post('/resetPassword', async (req, res) => {
            const randomString = genPassword(12);
            const { email } = req.body;

            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    const mailOptions = {
                        from: 'tanishqtemp10@gmail.com',
                        to: toEmail,
                        subject: 'Attention: requested new password',
                        text: `Here is your temporary password: ${randomString}. This password will expire in next 7 days. \nDon't forget to change the password by clicking on your profile image.`
                    };

                    transporter.sendMail(mailOptions, function (error, info) {
                        if (error) {
                            console.log(error);
                            res.send('Error sending email');
                        } else {
                            console.log('Email sent: ' + info.response);
                        }
                    });

                    const hashedPassword = await bcrypt.hash(randomString, 10);

                    const result = await users.updateOne(
                        { email: email },
                        { $set: { password: hashedPassword } }
                    );
                    if (result.modifiedCount === 1) {
                        res.status(200).json({ message: 'Password changed successfully!' });
                    } else {
                        res.status(400).json({ message: 'No changes made to the user.' });
                    }
                } else {
                    res.status(404).json({ message: "User not found" });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error updating the database', error: error });
            }
        });

        server.post('/changePassword', async (req, res) => {
            const { email, oldPassword, newPassword } = req.body;
            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    const isMatch = await bcrypt.compare(oldPassword, user.password);
                    if (isMatch) {
                        const hashedNewPassword = await bcrypt.hash(newPassword, 10);
                        const result = await users.updateOne(
                            { email: email },
                            { $set: { password: hashedNewPassword } }
                        );
                        if (result.modifiedCount === 1) {
                            res.status(200).json({ message: 'Password updated successfully!' });
                        } else {
                            res.status(400).json({ message: 'No changes made to the password.' });
                        }
                    } else {
                        res.status(401).json({ message: 'Invalid old password' });
                    }
                } else {
                    res.status(404).json({ message: "User not found" });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error updating the password', error: error });
            }
        });

        server.post('/healthStatus', async (req, res) => {
            const { email, healthStatus } = req.body;
            // console.log("POST /healthStatus");
            // console.log("Body");
            // console.log(req.body);
            // res.status(400).json( { message: 'not impemented'});
            try {
                const user = await users.findOne({ email: email });
                if (user) {
                    const result = await users.updateOne(
                        { email: email },
                        { $push: { healthStatus: { ...healthStatus, ts: new Date() } } });
                    if (result.modifiedCount === 1) {
                        res.status(200).json({ message: 'Health Status updated!' });
                    } else {
                        res.status(400).json({ message: 'No changes made to the status.' });
                    }
                } else {
                    res.status(404).json({ message: "User not found" });
                }
            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error updating the password', error: error });
            }
        });

        //stats rapid api
        server.get('/getCovidData', async (req, res) => {
            try {
                const axios = require('axios');
                const country = 'USA';
                const options = {
                    method: 'GET',
                    url: 'https://covid-19-tracking.p.rapidapi.com/v1/' + country,
                    headers: {
                        'X-RapidAPI-Key': process.env.RAPID_API_KEY,
                        'X-RapidAPI-Host': 'covid-19-tracking.p.rapidapi.com'
                    }
                };

                const response = await axios.request(options);
                const data = {
                    total_cases: response.data["Total Cases_text"],
                    total_deaths: response.data["Total Deaths_text"],
                    total_recovered: response.data["Total Recovered_text"]
                };
                res.status(200).json(data);

            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error fetching COVID data', error: error });
            }
        });

        // This uses query parameters! 
        server.get('/healthStatus', async (req, res) => {

            // console.log("GET /healthStatus");

            const email = req.query.email;

            if (!email) {
                return res.status(400).json({ message: 'Email parameter is required' });
            }

            try {
                const user = await users.findOne({ email: email }, { projection: { healthStatus: 1, _id: 0 } });
                // Sort the healthStatus array based on the ts field in descending order
                if (user && user.healthStatus) {
                    user.healthStatus.sort((a, b) => b.ts - a.ts);
                }
                // Limit the sorted array to 12 elements
                user.healthStatus = user.healthStatus.slice(0, 12);
                console.log(user);
                if (user && user.healthStatus) {
                    res.status(200).json({ "healthStatuses": user.healthStatus });
                } else {
                    res.status(404).json({ message: "User not found" });
                }

            } catch (error) {
                console.error(error);
                res.status(500).json({ message: 'Error updating the password', error: error });
            }
        });

        // stats
        server.get('/stats', async (req, res) => {
            const country = 'united states';
            try {
                const response = await axios.get('https://api.api-ninjas.com/v1/covid19', {
                    params: { country: country },
                    headers: { 'X-Api-Key': process.env.NINJAS_API_KEY }
                });

                if (response.status === 200) {
                    const data = response.data.map(item => {
                        const casesArray = Object.entries(item.cases).map(([date, caseDetails]) => ({
                            date,
                            numTotal: caseDetails.total,  // Renaming "total" to "numTotal"
                            numNew: caseDetails.new       // Renaming "new" to "numNew"
                        }));
                        return {
                            ...item,
                            cases: casesArray
                        };
                    });

                    //   console.log(data);
                    res.status(200).json({ stats: data });
                } else {
                    res.status(response.status).json({ message: "Error retrieving data", details: response.data });
                }
            } catch (error) {
                if (error.response) {
                    // Request made and server responded with a status code
                    // that falls out of the range of 2xx
                    console.error('Error:', error.response.status, error.response.data);
                    res.status(error.response.status).json({ message: "Error with API request", details: error.response.data });
                } else if (error.request) {
                    // The request was made but no response was received
                    console.error('Request Error:', error.request);
                    res.status(500).json({ message: "No response received from API" });
                } else {
                    // Something happened in setting up the request that triggered an Error
                    console.error('Error:', error.message);
                    res.status(500).json({ message: "Error making request to API", error: error.message });
                }
            }
        });

        server.get('/news', async (req, res) => {
            const country = "us"
            const category = "health"
            const removed = "[Removed]"
            try {
                const response = await axios.get('https://newsapi.org/v2/top-headlines', {
                    params: { apiKey: NEWS_API_KEY, country: country, category: category },
                });

                if (response.status === 200) {
                    const articlesArray = response.data.articles.filter(article => {
                        const { title, description, urlToImage, content } = article;
                        return title != removed && description != removed && urlToImage != removed && content != removed;
                    });

                    data = {
                        ...response.data,
                        articles: articlesArray
                    };
                    //   console.log(data);
                    res.status(200).json(data);
                } else {
                    res.status(response.status).json({ message: "Error retrieving data", details: response.data });
                }
            } catch (error) {
                if (error.response) {
                    // Request made and server responded with a status code
                    // that falls out of the range of 2xx
                    console.error('Error:', error.response.status, error.response.data);
                    res.status(error.response.status).json({ message: "Error with API request", details: error.response.data });
                } else if (error.request) {
                    // The request was made but no response was received
                    console.error('Request Error:', error.request);
                    res.status(500).json({ message: "No response received from API" });
                } else {
                    // Something happened in setting up the request that triggered an Error
                    console.error('Error:', error.message);
                    res.status(500).json({ message: "Error making request to API", error: error.message });
                }
            }
        });

        server.get('/nearby-hospitals', async (req, res) => {
            const ipInfoToken = process.env.IP_INFO_TOKEN;
            const GOOGLE_API_KEY = process.env.GOOGLE_API_KEY;

            try {
                const locResponse = await axios.get(`https://ipinfo.io?token=${ipInfoToken}`);
                const location = locResponse.data.loc;
                const radius = 8000;
                const type = 'hospital';

                const placesResponse = await axios.get(`https://maps.googleapis.com/maps/api/place/nearbysearch/json`, {
                    params: {
                        location,
                        radius,
                        type,
                        key: GOOGLE_API_KEY
                    }
                });

                const hospitals = placesResponse.data.results.map(place => {
                    const photoReference = place.photos && place.photos.length > 0 ? place.photos[0].photo_reference : null;
                    const openHours = place.opening_hours ? (place.opening_hours.open_now ? "Open now" : "Closed") : "Not available";
                    const imageUrl = photoReference ? `https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${photoReference}&key=${GOOGLE_API_KEY}` : 'No image available';

                    const numberOfBeds = Math.floor(Math.random() * 100);
                    const vaccineAvailable = Math.random() > 0.5 ? 'Yes' : 'No';
                    const locationString = `${place.geometry.location.lat}, ${place.geometry.location.lng}`;
                    const googleMapsLink = `https://www.google.com/maps/dir/?api=1&destination=${place.geometry.location.lat},${place.geometry.location.lng}&travelmode=driving`;

                    return {
                        name: place.name,
                        numberOfBeds: numberOfBeds,
                        vaccineAvailable: vaccineAvailable,
                        location: locationString,
                        address: place.vicinity,
                        openStatus: openHours,
                        imageUrl: imageUrl,
                        googleMapsLink: googleMapsLink
                    };
                });

                // console.log(hospitals);
                res.json(hospitals);
            } catch (error) {
                console.error('An error occurred while fetching data', error);
                res.status(500).send('An error occurred while fetching data');
            }
        });

        server.get('/nearby-pharmacy', async (req, res) => {
            const ipInfoToken = process.env.IP_INFO_TOKEN;
            const googleApiKey = process.env.GOOGLE_API_KEY;

            const { radius } = req.body;


            try {
                const locResponse = await axios.get(`https://ipinfo.io?token=${ipInfoToken}`);
                const location = locResponse.data.loc;
                const radius = radius;
                const fields = 'name,geometry,formatted_address,formatted_phone_number';
                const type = 'hospital';

                const placesResponse = await axios.get(`https://maps.googleapis.com/maps/api/place/nearbysearch/json`, {
                    params: {
                        location,
                        radius,
                        type,
                        key: googleApiKey,
                        fields
                    }
                });

                const pharmacies = placesResponse.data.results.map(pharmacy => {
                    const vaccineAvailable = Math.random() > 0.5 ? 'Yes' : 'No';

                    return {
                        name: pharmacy.name,
                        vaccineAvailable: vaccineAvailable,
                        location: pharmacy.geometry.location,
                        address: pharmacy.formatted_address || 'Not available',
                        phoneNumber: pharmacy.formatted_phone_number || 'Not available'
                    };
                });

                res.json(pharmacies);
            } catch (error) {
                console.error(error);
                res.status(500).send('An error occurred while fetching data');
            }
        });

        server.get('/', (req, res) => {
            res.send('Server Successful!');
        });

        server.listen(PORT, () => {
            console.log(`Server running on http://localhost:${PORT}`);
        });
    })
    .catch(err => {
        console.error("Failed to connect to MongoDB:", err);
        process.exit(1);
    });

process.on('SIGINT', () => {
    client.close().then(() => {
        console.log('MongoDB connection closed');
        process.exit(0);
    });
});
