An implementation of multinomial and multivariate naive bayes spam filter.

  The naive Bayes model is a popular model for dealing with textual,
categorical and mixed categorical/real-valued data. Its main shortcoming as a probabilistic
model – poorly calibrated probability estimates – are outweighed by generally
good ranking performance. Another apparent paradox with naive Bayes is that it isn’t
particularly Bayesian at all! For one thing, we have seen that the poor probability estimates
necessitate the use of reweighted likelihoods, which avoids using Bayes’ rule
altogether. Secondly, in training a naive Bayes model we use maximum-likelihood parameter
estimation, whereas a fully fledged Bayesian approach would not commit to a
particular parameter value, but rather employ a full posterior distribution. Personally, I
think the essence of naive Bayes is the decomposition of joint likelihoods into marginal
likelihoods.[1]

[1] Machine Learning The Art and Science of Algorithmsthat Make Sense of Data
PETER FLACH
