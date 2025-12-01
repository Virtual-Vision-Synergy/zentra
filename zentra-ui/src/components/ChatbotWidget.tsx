import React, { useState, useEffect, useRef } from 'react';
import { chatbotAPI } from '../services/aiService';
import '../styles/ChatbotWidget.css';

interface Message {
  id: number;
  message: string;
  response: string;
  timestamp: string;
  isUser: boolean;
}

interface ChatbotWidgetProps {
  userId?: number;
}

const ChatbotWidget: React.FC<ChatbotWidgetProps> = ({ userId }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputMessage, setInputMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [sessionId] = useState(`session_${Date.now()}_${Math.random()}`);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (isOpen && messages.length === 0) {
      // Load session history
      loadHistory();
    }
  }, [isOpen]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const loadHistory = async () => {
    try {
      const response = await chatbotAPI.getSessionHistory(sessionId);
      if (response.data && response.data.length > 0) {
        const formattedMessages = response.data.flatMap((msg: any) => [
          {
            id: msg.id * 2 - 1,
            message: msg.message,
            response: '',
            timestamp: msg.timestamp,
            isUser: true,
          },
          {
            id: msg.id * 2,
            message: '',
            response: msg.response,
            timestamp: msg.timestamp,
            isUser: false,
          },
        ]);
        setMessages(formattedMessages);
      }
    } catch (error) {
      console.error('Error loading chat history:', error);
    }
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleSendMessage = async () => {
    if (!inputMessage.trim()) return;

    const userMessage = inputMessage.trim();
    setInputMessage('');
    setIsLoading(true);

    // Add user message to chat
    const tempUserMsg: Message = {
      id: Date.now(),
      message: userMessage,
      response: '',
      timestamp: new Date().toISOString(),
      isUser: true,
    };
    setMessages((prev) => [...prev, tempUserMsg]);

    try {
      const response = await chatbotAPI.sendMessage(sessionId, userMessage, userId);
      const botMessage: Message = {
        id: Date.now() + 1,
        message: '',
        response: response.data.response,
        timestamp: response.data.timestamp,
        isUser: false,
      };
      setMessages((prev) => [...prev, botMessage]);
    } catch (error) {
      console.error('Error sending message:', error);
      const errorMessage: Message = {
        id: Date.now() + 1,
        message: '',
        response: "Désolé, une erreur s'est produite. Veuillez réessayer.",
        timestamp: new Date().toISOString(),
        isUser: false,
      };
      setMessages((prev) => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const quickQuestions = [
    'Comment demander un congé ?',
    'Quand est-ce que je reçois mon salaire ?',
    'Comment pointer mes heures ?',
    'Comment obtenir une attestation ?',
  ];

  const handleQuickQuestion = (question: string) => {
    setInputMessage(question);
  };

  return (
    <>
      {/* Floating button */}
      <button
        className={`chatbot-toggle-btn ${isOpen ? 'open' : ''}`}
        onClick={() => setIsOpen(!isOpen)}
        title="Assistant RH"
      >
        {isOpen ? '✕' : '💬'}
      </button>

      {/* Chat window */}
      {isOpen && (
        <div className="chatbot-widget">
          <div className="chatbot-header">
            <div className="chatbot-header-content">
              <div className="chatbot-avatar">🤖</div>
              <div>
                <h3>Assistant RH Zentra</h3>
                <span className="chatbot-status">En ligne</span>
              </div>
            </div>
            <button className="chatbot-close-btn" onClick={() => setIsOpen(false)}>
              ✕
            </button>
          </div>

          <div className="chatbot-messages">
            {messages.length === 0 && (
              <div className="chatbot-welcome">
                <div className="chatbot-welcome-avatar">👋</div>
                <h4>Bienvenue sur l'Assistant RH !</h4>
                <p>Je suis là pour répondre à vos questions sur les congés, la paie, les horaires et plus encore.</p>
                <div className="chatbot-quick-questions">
                  <p>Questions fréquentes :</p>
                  {quickQuestions.map((question, index) => (
                    <button
                      key={index}
                      className="quick-question-btn"
                      onClick={() => handleQuickQuestion(question)}
                    >
                      {question}
                    </button>
                  ))}
                </div>
              </div>
            )}

            {messages.map((msg) => (
              <div
                key={msg.id}
                className={`chatbot-message ${msg.isUser ? 'user-message' : 'bot-message'}`}
              >
                {!msg.isUser && <div className="message-avatar">🤖</div>}
                <div className="message-content">
                  <div className="message-text">
                    {msg.isUser ? msg.message : msg.response}
                  </div>
                  <div className="message-time">
                    {new Date(msg.timestamp).toLocaleTimeString('fr-FR', {
                      hour: '2-digit',
                      minute: '2-digit',
                    })}
                  </div>
                </div>
              </div>
            ))}

            {isLoading && (
              <div className="chatbot-message bot-message">
                <div className="message-avatar">🤖</div>
                <div className="message-content">
                  <div className="typing-indicator">
                    <span></span>
                    <span></span>
                    <span></span>
                  </div>
                </div>
              </div>
            )}

            <div ref={messagesEndRef} />
          </div>

          <div className="chatbot-input">
            <textarea
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              onKeyDown={handleKeyDown}
              placeholder="Posez votre question..."
              rows={1}
              disabled={isLoading}
            />
            <button
              onClick={handleSendMessage}
              disabled={!inputMessage.trim() || isLoading}
              className="send-btn"
            >
              ➤
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default ChatbotWidget;

