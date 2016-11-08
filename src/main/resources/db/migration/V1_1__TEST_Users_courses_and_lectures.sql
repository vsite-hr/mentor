COPY users (user_id, user_email, user_password, user_name) FROM stdin;
5da6bf98-a467-11e6-aedd-4485006d0fd8	nikola@vsite.hr	:2YIF1lkxFYc8.	Nikola Tesla
79c8cbbc-a467-11e6-aedd-4485006d0fd8	rudjer@vsite.hr	:XV9al36evoh9k	Ruđer Bošković
89075120-a467-11e6-aedd-4485006d0fd8	lavoslav@vsite.hr	:iPz8xk5Ns0aV.	Lavoslav Ružička
938f9684-a467-11e6-aedd-4485006d0fd8	vladimir@vsite.hr	:CYO/q68iDeFfw	Vladimir Prelog
9b1e16e6-a467-11e6-aedd-4485006d0fd8	faust@vsite.hr	:d4gXNs7xn4r1Q	Faust Vrančić
\.


COPY courses (course_id, course_title, course_description, author_id) FROM stdin;
f0aabc82-a475-11e6-aedd-4485006d0fd8	Alternating current	Alternating current (AC), is an electric current in which the flow of electric charge periodically reverses direction	5da6bf98-a467-11e6-aedd-4485006d0fd8
\.


COPY lectures (lecture_id, lecture_title, lecture_description, author_id) FROM stdin;
200ceefa-a476-11e6-aedd-4485006d0fd8	Transmission	Electric power transmission is the bulk movement of electrical energy from a generating site, such as a power plant, to an electrical substation	5da6bf98-a467-11e6-aedd-4485006d0fd8
3b6a8d4c-a476-11e6-aedd-4485006d0fd8	Distribution	Electric power distribution is the final stage in the delivery of electric power; it carries electricity from the transmission system to individual consumers	5da6bf98-a467-11e6-aedd-4485006d0fd8
\.


COPY course_lectures (course_id, lecture_id, lecture_ordinal) FROM stdin;
f0aabc82-a475-11e6-aedd-4485006d0fd8	200ceefa-a476-11e6-aedd-4485006d0fd8	1
f0aabc82-a475-11e6-aedd-4485006d0fd8	3b6a8d4c-a476-11e6-aedd-4485006d0fd8	2
\.


COPY units_text (unit_id, unit_type, unit_title, author_id, unit_markup_type, unit_markup) FROM stdin;
b8ebe73e-a476-11e6-aedd-4485006d0fd8	Text	Overhead transmission	5da6bf98-a467-11e6-aedd-4485006d0fd8	None	High-voltage overhead conductors are not covered by insulation. The conductor material is nearly always an aluminum alloy, made into several strands and possibly reinforced with steel strands. Copper was sometimes used for overhead transmission, but aluminum is lighter, yields only marginally reduced performance and costs much less. Overhead conductors are a commodity supplied by several companies worldwide. Improved conductor material and shapes are regularly used to allow increased capacity and modernize transmission circuits. Conductor sizes range from 12 mm2 (#6 American wire gauge) to 750 mm2 (1,590,000 circular mils area), with varying resistance and current-carrying capacity. Thicker wires would lead to a relatively small increase in capacity due to the skin effect, that causes most of the current to flow close to the surface of the wire. Because of this current limitation, multiple parallel cables (called bundle conductors) are used when higher capacity is needed. Bundle conductors are also used at high voltages to reduce energy loss caused by corona discharge.\n\nToday, transmission-level voltages are usually considered to be 110 kV and above. Lower voltages, such as 66 kV and 33 kV, are usually considered subtransmission voltages, but are occasionally used on long lines with light loads. Voltages less than 33 kV are usually used for distribution. Voltages above 765 kV are considered extra high voltage and require different designs compared to equipment used at lower voltages.\n\nSince overhead transmission wires depend on air for insulation, the design of these lines requires minimum clearances to be observed to maintain safety. Adverse weather conditions, such as high wind and low temperatures, can lead to power outages. Wind speeds as low as 23 knots (43 km/h) can permit conductors to encroach operating clearances, resulting in a flashover and loss of supply.[2] Oscillatory motion of the physical line can be termed gallop or flutter depending on the frequency and amplitude of oscillation.
cd519d4a-a476-11e6-aedd-4485006d0fd8	Text	Underground transmission	5da6bf98-a467-11e6-aedd-4485006d0fd8	None	Electric power can also be transmitted by underground power cables instead of overhead power lines. Underground cables take up less right-of-way than overhead lines, have lower visibility, and are less affected by bad weather. However, costs of insulated cable and excavation are much higher than overhead construction. Faults in buried transmission lines take longer to locate and repair. Underground lines are strictly limited by their thermal capacity, which permits less overload or re-rating than overhead lines. Long underground AC cables have significant capacitance, which may reduce their ability to provide useful power to loads beyond 50 mi (80 km). Long underground DC cables have no such issue and can run for thousands of miles.
7e0e2e68-a478-11e6-aedd-4485006d0fd8	Text	Primary distribution	5da6bf98-a467-11e6-aedd-4485006d0fd8	None	Primary distribution voltages are 22kV or 11 kV.[10] Only large consumers are fed directly from distribution voltages; most utility customers are connected to a transformer, which reduces the distribution voltage to the low voltage used by lighting and interior wiring systems.\n\nVoltage varies according to its role in the supply and distribution system. According to international standards, there are initially two voltage groups: low voltage (LV): up to and including 1kV AC (or 1.5kV DC) and high voltage (HV): above 1 kV AC (or 1.5 kV DC).
91737440-a478-11e6-aedd-4485006d0fd8	Text	Secondary distribution	5da6bf98-a467-11e6-aedd-4485006d0fd8	None	Electricity is delivered at a frequency of either 50 or 60 Hz, depending on the region. It is delivered to domestic customers as single-phase electric power In some countries as in Europe a three phase supply may be made available for larger properties. Seen in an oscilloscope, the domestic power supply in North America would look like a sine wave, oscillating between -170 volts and 170 volts, giving an effective voltage of 120 volts.[18] Three-phase power is more efficient in terms of power delivered per cable used, and is more suited to running large electric motors. Some large European appliances may be powered by three-phase power, such as electric stoves and clothes dryers.\n\nA ground connection is normally provided for the customer's system as well as for the equipment owned by the utility. The purpose of connecting the customer's system to ground is to limit the voltage that may develop if high voltage conductors fall down onto lower-voltage conductors which are usually mounted lower to the ground, or if a failure occurs within a distribution transformer. Earthing systems can be TT, TN-S, TN-C-S or TN-C.
\.


COPY lecture_units (lecture_id, unit_id, unit_ordinal) FROM stdin;
200ceefa-a476-11e6-aedd-4485006d0fd8	b8ebe73e-a476-11e6-aedd-4485006d0fd8	1
200ceefa-a476-11e6-aedd-4485006d0fd8	cd519d4a-a476-11e6-aedd-4485006d0fd8	2
3b6a8d4c-a476-11e6-aedd-4485006d0fd8	7e0e2e68-a478-11e6-aedd-4485006d0fd8	1
3b6a8d4c-a476-11e6-aedd-4485006d0fd8	91737440-a478-11e6-aedd-4485006d0fd8	2
\.
